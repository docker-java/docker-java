package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.core.util.CompressArchiveUtil;

@Test(groups = "integration")
public class CopyArchiveToContainerCmdImplTest extends AbstractDockerClientTest {
    @BeforeTest
    public void beforeTest() throws Exception {
        super.beforeTest();
    }

    @AfterTest
    public void afterTest() {
        super.afterTest();
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        super.beforeMethod(method);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        super.afterMethod(result);
    }

    @Test
    public void copyFileToContainer() throws Exception {
        CreateContainerResponse container = prepareContainerForCopy();
        Path temp = Files.createTempFile("", ".tar.gz");
        CompressArchiveUtil.tar(Paths.get("src/test/resources/testReadFile"), temp, true, false);
        try (InputStream uploadStream = Files.newInputStream(temp)) {
            dockerClient.copyArchiveToContainerCmd(container.getId()).withTarInputStream(uploadStream).exec();
            assertFileCopied(container);
        }
    }

    @Test
    public void copyStreamToContainer() throws Exception {
        CreateContainerResponse container = prepareContainerForCopy();
        dockerClient.copyArchiveToContainerCmd(container.getId()).withHostResource("src/test/resources/testReadFile")
                .exec();
        assertFileCopied(container);
    }

    private CreateContainerResponse prepareContainerForCopy() {
        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withName("docker-java-itest-copyToContainer").exec();
        LOG.info("Created container: {}", container);
        assertThat(container.getId(), not(isEmptyOrNullString()));
        dockerClient.startContainerCmd(container.getId()).exec();
        // Copy a folder to the container
        return container;
    }

    private void assertFileCopied(CreateContainerResponse container) throws IOException {
        try (InputStream response = dockerClient.copyArchiveFromContainerCmd(container.getId(), "testReadFile").exec()) {
            boolean bytesAvailable = response.available() > 0;
            assertTrue(bytesAvailable, "The file was not copied to the container.");
        }
    }

    @Test
    public void copyToNonExistingContainer() throws Exception {
        try {
            dockerClient.copyArchiveToContainerCmd("non-existing").withHostResource("src/test/resources/testReadFile")
                    .exec();
            fail("expected NotFoundException");
        } catch (NotFoundException ignored) {
        }
    }
    
    @Test 
    public void copyDirWithLastAddedTarEnryEmptyDir() throws Exception{
        // create a temp dir
        Path localDir = Files.createTempDirectory("");
        localDir.toFile().deleteOnExit();
        // create sub-dir with name b
        Path emptyDir = Files.createTempDirectory(localDir.resolve("b"), "");
        emptyDir.toFile().deleteOnExit();
        // creaet sub-dir with name a
        Path dirWithFile = Files.createTempDirectory(localDir.resolve("a"), "");
        dirWithFile.toFile().deleteOnExit();
        // create file in sub-dir b, name or conter are irrelevant
        Path file = Files.createTempFile(dirWithFile.resolve("file"), "", "");
        file.toFile().deleteOnExit();
        
        // create a test container
        CreateContainerResponse container = dockerClient.createContainerCmd("progrium/busybox:latest")
                .withCmd("/bin/sh", "-c", "while true; do sleep 9999; done")
                .exec();
        // start the container
        dockerClient.startContainerCmd(container.getId()).exec();
        // copy data from local dir to container 
        dockerClient.copyArchiveToContainerCmd(container.getId())
                .withHostResource(localDir.toString())
                .exec();
    }

}
