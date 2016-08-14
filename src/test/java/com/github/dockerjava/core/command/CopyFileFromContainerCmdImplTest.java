package com.github.dockerjava.core.command;

import static com.github.dockerjava.utils.TestUtils.getVersion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

import java.io.InputStream;
import java.lang.reflect.Method;

import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.utils.TestUtils;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class CopyFileFromContainerCmdImplTest extends AbstractDockerClientTest {

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
    public void copyFromContainer() throws Exception {
        if (getVersion(dockerClient).isGreaterOrEqual(RemoteApiVersion.VERSION_1_24)) {
            throw new SkipException("Doesn't work since 1.24");
        }

        // TODO extract this into a shared method
        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withName("docker-java-itest-copyFromContainer").withCmd("touch", "/copyFromContainer").exec();

        LOG.info("Created container: {}", container);
        assertThat(container.getId(), not(isEmptyOrNullString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InputStream response = dockerClient.copyFileFromContainerCmd(container.getId(), "/copyFromContainer").exec();
        Boolean bytesAvailable = response.available() > 0;
        assertTrue(bytesAvailable, "The file was not copied from the container.");

        // read the stream fully. Otherwise, the underlying stream will not be closed.
        String responseAsString = asString(response);
        assertNotNull(responseAsString);
        assertTrue(responseAsString.length() > 0);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void copyFromNonExistingContainer() throws Exception {

        dockerClient.copyFileFromContainerCmd("non-existing", "/test").exec();
    }
}
