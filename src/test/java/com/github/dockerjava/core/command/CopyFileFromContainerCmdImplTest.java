package com.github.dockerjava.core.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;

import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.InputStream;
import java.lang.reflect.Method;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Test(groups = "integration")
public class CopyFileFromContainerCmdImplTest extends AbstractDockerClientTest {

    @BeforeTest
    public void beforeTest() {
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
    public void copyFromContainer() throws Exception {
        // TODO extract this into a shared method
        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withName("docker-java-itest-copyFromContainer")
                .withCmd("touch", "/copyFromContainer")
                .exec();

        LOG.info("Created container: {}", container);
        assertThat(container.getId(), not(isEmptyOrNullString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InputStream response = dockerClient.copyFileFromContainerCmd(container.getId(), "/copyFromContainer").exec();
        boolean bytesAvailable = response.available() > 0;
        assertTrue(bytesAvailable, "The file was not copied from the container.");

        // read the stream fully. Otherwise, the underlying stream will not be closed.
        String responseAsString = asString(response);
        assertNotNull(responseAsString);
        assertTrue(responseAsString.length() > 0);
    }
    
    @Test
    public void copyFromNonExistingContainer() throws Exception {
        try {
            dockerClient.copyFileFromContainerCmd("non-existing", "/test").exec();
            fail("expected NotFoundException");
        } catch (NotFoundException ignored) {
        }
    }
}
