package com.github.dockerjava.netty.exec;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_22;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_23;
import static com.github.dockerjava.utils.TestUtils.getVersion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import com.github.dockerjava.core.RemoteApiVersion;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;

@Test(groups = "integration")
public class ExecStartCmdExecTest extends AbstractNettyDockerClientTest {
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

    @Test()
    public void execStart() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout(true)
                .withCmd("touch", "/execStartTest.log")
                .exec();

        dockerClient.execStartCmd(execCreateCmdResponse.getId())
                .withDetach(false)
                .exec(new ExecStartResultCallback(System.out, System.err))
                .awaitCompletion();

        InputStream response = dockerClient.copyArchiveFromContainerCmd(container.getId(), "/execStartTest.log").exec();

        Boolean bytesAvailable = response.available() > 0;
        assertTrue(bytesAvailable, "The file was not copied from the container.");

        // read the stream fully. Otherwise, the underlying stream will not be closed.
        String responseAsString = asString(response);
        assertNotNull(responseAsString);
        assertTrue(responseAsString.length() > 0);
    }

    @Test()
    public void execStartAttached() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout(true)
                .withCmd("touch", "/execStartTest.log")
                .exec();

        dockerClient.execStartCmd(execCreateCmdResponse.getId())
                .withDetach(false)
                .exec(new ExecStartResultCallback(System.out, System.err))
                .awaitCompletion();

        InputStream response = dockerClient.copyArchiveFromContainerCmd(container.getId(), "/execStartTest.log").exec();
        Boolean bytesAvailable = response.available() > 0;
        assertTrue(bytesAvailable, "The file was not copied from the container.");

        // read the stream fully. Otherwise, the underlying stream will not be closed.
        String responseAsString = asString(response);
        assertNotNull(responseAsString);
        assertTrue(responseAsString.length() > 0);
    }

    @Test()
    public void execStartAttachStdin() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InputStream stdin = new ByteArrayInputStream("STDIN\n".getBytes("UTF-8"));

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();

        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout(true)
                .withAttachStdin(true)
                .withCmd("cat")
                .exec();

        boolean completed = dockerClient.execStartCmd(execCreateCmdResponse.getId())
                .withDetach(false)
                .withTty(true)
                .withStdIn(stdin)
                .exec(new ExecStartResultCallback(stdout, System.err))
                .awaitCompletion(5, TimeUnit.SECONDS);

        assertTrue(completed, "The process was not finished.");
        assertEquals(stdout.toString("UTF-8"), "STDIN\n");
    }

    @Test()
    public void execStartAttachStdinToShell() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient
                .createContainerCmd("busybox")
                .withCmd("sleep", "9999")
                .withName(containerName)
                .exec();

        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InputStream stdin = new ByteArrayInputStream("ls\n".getBytes());

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();

        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout(true)
                .withAttachStdin(true)
                .withTty(false)
                .withCmd("/bin/sh").exec();

        dockerClient.execStartCmd(execCreateCmdResponse.getId())
                .withDetach(false)
                .withStdIn(stdin)
                .exec(new ExecStartResultCallback(stdout, System.err))
                .awaitCompletion();

        assertThat(stdout.toString(), containsString("etc\n"));
    }

    @Test()
    public void execStartNotAttachedStdin() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InputStream stdin = new ByteArrayInputStream("echo STDIN\n".getBytes());

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();

        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout(true)
                .withAttachStdin(false)
                .withCmd("/bin/sh")
                .exec();

        boolean completed = dockerClient.execStartCmd(execCreateCmdResponse.getId())
                .withDetach(false)
                .withStdIn(stdin)
                .exec(new ExecStartResultCallback(stdout, System.err))
                .awaitCompletion(5, TimeUnit.SECONDS);

        assertEquals(stdout.toString(), "");

        if (getVersion(dockerClient).isGreaterOrEqual(VERSION_1_23)) {
            assertFalse(completed, "The process was not finished.");
        } else {
            assertTrue(completed, "with v1.22 of the remote api the server closed the connection when no stdin " +
                    "was attached while exec create, so completed was true");
        }
    }
}
