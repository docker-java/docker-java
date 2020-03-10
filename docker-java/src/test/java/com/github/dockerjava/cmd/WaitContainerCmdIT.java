package com.github.dockerjava.cmd;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.WaitResponse;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

public class WaitContainerCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(BuildImageCmd.class);

    @Test
    public void testWaitContainer() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("true").exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        int exitCode = dockerRule.getClient().waitContainerCmd(container.getId()).start()
                .awaitStatusCode();
        LOG.info("Container exit code: {}", exitCode);

        assertThat(exitCode, equalTo(0));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        assertThat(inspectContainerResponse.getState().getRunning(), is(equalTo(false)));
        assertThat(inspectContainerResponse.getState().getExitCode(), is(equalTo(exitCode)));
    }

    @Test(expected = NotFoundException.class)
    public void testWaitNonExistingContainer() throws Exception {

        ResultCallback.Adapter<WaitResponse> callback = new ResultCallback.Adapter<WaitResponse>() {
            public void onNext(WaitResponse waitResponse) {
                throw new AssertionError("expected NotFoundException");
            }
        };

        dockerRule.getClient().waitContainerCmd("non-existing").exec(callback).awaitCompletion();
    }

    @Test
    public void testWaitContainerAbort() throws Exception {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        WaitContainerResultCallback callback = dockerRule.getClient().waitContainerCmd(container.getId()).start();

        Thread.sleep(5000);

        callback.close();

        dockerRule.getClient().killContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        assertThat(inspectContainerResponse.getState().getRunning(), is(equalTo(false)));
    }

    @Test
    public void testWaitContainerTimeout() throws Exception {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "10").exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        WaitContainerResultCallback callback = dockerRule.getClient().waitContainerCmd(container.getId()).exec(
                new WaitContainerResultCallback());
        try {
            callback.awaitStatusCode(100, TimeUnit.MILLISECONDS);
            throw new AssertionError("Should throw exception on timeout.");
        } catch (DockerClientException e) {
            LOG.info(e.getMessage());
        }
    }
}
