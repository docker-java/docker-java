package com.github.dockerjava.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.command.WaitContainerResultCallback;

/**
 * Unit test for DockerClient.
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
@Test(groups = "integration")
public class DockerClientTest extends AbstractDockerClientTest {
    public static final Logger LOG = LoggerFactory.getLogger(DockerClientTest.class);

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
    public void testRunShlex() throws DockerException {

        String[] commands = new String[] {
                "true",
                "echo \"The Young Descendant of Tepes & Septette for the Dead Princess\"",
                "echo -n 'The Young Descendant of Tepes & Septette for the Dead Princess'",
                "/bin/sh -c echo Hello World", "/bin/sh -c echo 'Hello World'", "echo 'Night of Nights'",
                "true && echo 'Night of Nights'"
        };

        for (String command : commands) {
            LOG.info("Running command: [{}]", command);

            CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                    .withCmd(command)
                    .exec();
            dockerClient.startContainerCmd(container.getId());

            int exitcode = dockerClient.waitContainerCmd(container.getId())
                    .exec(new WaitContainerResultCallback())
                    .awaitStatusCode();

            assertThat(exitcode, equalTo(0));
        }
    }

}
