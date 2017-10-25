package com.github.dockerjava.cmd;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.core.command.PullImageCmdImpl;
import com.github.dockerjava.core.command.PullImageResultCallback;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.utils.TestUtils.getVersion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class PullImageCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(PullImageCmdIT.class);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static final PullImageCmd.Exec NOP_EXEC = new PullImageCmd.Exec() {
        public Void exec(PullImageCmd command, ResultCallback<PullResponseItem> resultCallback) {
            return null;
        };
    };

    @Test
    public void nullAuthConfig() throws Exception {
        PullImageCmdImpl pullImageCmd = new PullImageCmdImpl(NOP_EXEC, null, "");
        try {
            pullImageCmd.withAuthConfig(null);
            throw new AssertionError();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "authConfig was not specified");
        } finally {
            pullImageCmd.close();
        }
    }

    @Test
    public void testPullImage() throws Exception {
        Info info = dockerRule.getClient().infoCmd().exec();
        LOG.info("Client info: {}", info.toString());

        int imgCount = info.getImages();
        LOG.info("imgCount1: {}", imgCount);

        // This should be an image that is not used by other repositories
        // already
        // pulled down, preferably small in size. If tag is not used pull will
        // download all images in that repository but tmpImgs will only
        // deleted 'latest' image but not images with other tags
        String testImage = "hackmann/empty";

        LOG.info("Removing image: {}", testImage);

        try {
            dockerRule.getClient().removeImageCmd(testImage).withForce(true).exec();
        } catch (NotFoundException e) {
            // just ignore if not exist
        }

        info = dockerRule.getClient().infoCmd().exec();
        LOG.info("Client info: {}", info.toString());

        imgCount = info.getImages();
        LOG.info("imgCount2: {}", imgCount);

        LOG.info("Pulling image: {}", testImage);

        dockerRule.getClient().pullImageCmd(testImage).exec(new PullImageResultCallback()).awaitCompletion();

        info = dockerRule.getClient().infoCmd().exec();
        LOG.info("Client info after pull, {}", info.toString());

        assertThat(imgCount, lessThanOrEqualTo(info.getImages()));

        InspectImageResponse inspectImageResponse = dockerRule.getClient().inspectImageCmd(testImage).exec();
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());
        assertThat(inspectImageResponse, notNullValue());
    }



    @Test
    public void testPullNonExistingImage() throws Exception {
        // does not throw an exception

        if (getVersion(dockerRule.getClient()).equals(RemoteApiVersion.VERSION_1_24)) {
            // 1.24 - no exception
        } else if (getVersion(dockerRule.getClient())
                .isGreaterOrEqual(RemoteApiVersion.VERSION_1_26)) {
            exception.expect(NotFoundException.class);
        } else {
            exception.expect(DockerClientException.class);
        }

        // stream needs to be fully read in order to close the underlying connection
        dockerRule.getClient().pullImageCmd("xvxcv/foo")
                .exec(new PullImageResultCallback())
                .awaitCompletion(30, TimeUnit.SECONDS);
    }

}
