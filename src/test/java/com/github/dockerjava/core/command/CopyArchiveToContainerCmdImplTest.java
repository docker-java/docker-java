package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
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

    @Test(expectedExceptions = NotFoundException.class)
    public void copyToNonExistingContainer() throws Exception {

        dockerClient.copyArchiveToContainerCmd("non-existing").withHostResource("src/test/resources/testReadFile").exec();
    }

    @Test
    public void copyDirWithLastAddedTarEntryEmptyDir() throws Exception{
        // create a temp dir
        Path localDir = Files.createTempDirectory(null);
        localDir.toFile().deleteOnExit();
        // create empty sub-dir with name b
        Files.createDirectory(localDir.resolve("b"));
        // create sub-dir with name a
        Path dirWithFile = Files.createDirectory(localDir.resolve("a"));
        // create file in sub-dir b, name or conter are irrelevant
        Files.createFile(dirWithFile.resolve("file"));

        // create a test container
        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withCmd("sleep", "9999")
                .exec();
        // start the container
        dockerClient.startContainerCmd(container.getId()).exec();
        // copy data from local dir to container
        dockerClient.copyArchiveToContainerCmd(container.getId())
                .withHostResource(localDir.toString())
                .exec();

        // cleanup dir
        FileUtils.deleteDirectory(localDir.toFile());
    }
    
    @Test
    public void copyFileWithExecutePermission() throws Exception {
        // create script file, add permission to execute
        Path scriptPath = Files.createTempFile("run", ".sh");
        boolean executable = scriptPath.toFile().setExecutable(true, false);
        if (!executable){
            throw new Exception("Execute permission on file not set!");
        }
        String snippet = "Running script with execute permission.";
        String scriptTextStr = "#!/bin/sh\necho \"" + snippet + "\"";
        // write content for created script
        Files.write(scriptPath, scriptTextStr.getBytes());
        // create a test container which starts and waits 3 seconds for the
        // script to be copied to the container's home dir and then executes it
        String containerCmd = "sleep 3; /home/" + scriptPath.getFileName().toString();
        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withName("test")
                .withCmd("/bin/sh", "-c", containerCmd)
                .exec();
        // start the container
        dockerClient.startContainerCmd(container.getId()).exec();
        // copy script to container home dir
        dockerClient.copyArchiveToContainerCmd(container.getId())
                .withRemotePath("/home")
                .withHostResource(scriptPath.toString())
                .exec();
        // await exid code
        int exitCode = dockerClient.waitContainerCmd(container.getId())
                .exec(new WaitContainerResultCallback())
                .awaitStatusCode();
        // check result
        assertThat(exitCode, equalTo(0));
    }

}
