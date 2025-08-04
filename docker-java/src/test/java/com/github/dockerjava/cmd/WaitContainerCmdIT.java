package com.github.dockerjava.cmd;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.WaitContainerCondition;
import com.github.dockerjava.api.model.WaitResponse;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_25;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_30;
import static com.github.dockerjava.junit.DockerMatchers.isGreaterOrEqual;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeThat;

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
    public void testWaitContainerTimeout() {

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

    @Test
    public void testWaitNotStartedContainer() {
        assumeThat("API version should be > 1.25", dockerRule, isGreaterOrEqual(VERSION_1_25));

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
            .withHostConfig(newHostConfig().withAutoRemove(true))
            .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        WaitContainerResultCallback callback = dockerRule.getClient().waitContainerCmd(container.getId()).exec(new WaitContainerResultCallback());

        Integer statusCode = callback.awaitStatusCode(100, TimeUnit.MILLISECONDS);
        Assert.assertEquals(0, statusCode.intValue());
    }

    @Test
    public void testWaitContainerWithAutoRemoval() {
        assumeThat("API version should be > 1.30", dockerRule, isGreaterOrEqual(VERSION_1_30));

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
            .withCmd("false")
            .withHostConfig(newHostConfig().withAutoRemove(true))
            .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        WaitContainerResultCallback removedCondition = dockerRule.getClient().waitContainerCmd(container.getId())
            .withCondition(WaitContainerCondition.REMOVED)
            .exec(new WaitContainerResultCallback());

        WaitContainerResultCallback nextExitCondition = dockerRule.getClient().waitContainerCmd(container.getId())
            .withCondition(WaitContainerCondition.NEXT_EXIT)
            .exec(new WaitContainerResultCallback());

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        Assert.assertEquals(1, removedCondition.awaitStatusCode(100, TimeUnit.MILLISECONDS).intValue());
        Assert.assertEquals(1, nextExitCondition.awaitStatusCode(100, TimeUnit.MILLISECONDS).intValue());
    }

    @Test
    public void testWaitRestartedContainer() {
        assumeThat("API version should be > 1.30", dockerRule, isGreaterOrEqual(VERSION_1_30));

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
            .withCmd("sh", "-c", "[ -f \"$HOME/.first_run_marker\" ] && exit 2 || { touch \"$HOME/.first_run_marker\"; exit 1; }")
            .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        WaitContainerResultCallback firstExitCallback = dockerRule.getClient().waitContainerCmd(container.getId())
            .withCondition(WaitContainerCondition.NEXT_EXIT)
            .exec(new WaitContainerResultCallback());

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        Integer firstStatusCode = firstExitCallback.awaitStatusCode(100, TimeUnit.MILLISECONDS);
        Assert.assertEquals(1, firstStatusCode.intValue());

        WaitContainerResultCallback callback = dockerRule.getClient().waitContainerCmd(container.getId())
            .withCondition(WaitContainerCondition.NEXT_EXIT)
            .exec(new WaitContainerResultCallback());

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        Integer statusCode = callback.awaitStatusCode(100, TimeUnit.MILLISECONDS);
        Assert.assertEquals(2, statusCode.intValue());
    }
}
