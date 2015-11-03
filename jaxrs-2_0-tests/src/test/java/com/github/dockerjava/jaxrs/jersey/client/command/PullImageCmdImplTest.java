package com.github.dockerjava.jaxrs.jersey.client.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.command.PullImageCmdImpl;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.jaxrs.jersey.client.client.AbstractDockerClientTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

@Test(groups = "integration")
public class PullImageCmdImplTest extends AbstractDockerClientTest {

    private static final PullImageCmd.Exec NOP_EXEC = new PullImageCmd.Exec() {
        public Void exec(PullImageCmd command, ResultCallback<PullResponseItem> resultCallback) {
            return null;
        };
    };

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
    public void nullAuthConfig() throws Exception {
        PullImageCmdImpl pullImageCmd = new PullImageCmdImpl(NOP_EXEC, null, "");
        try {
            pullImageCmd.withAuthConfig(null);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "authConfig was not specified");
        } finally {
            pullImageCmd.close();
        }
    }

    @Test
    public void testPullImage() throws Exception {
        Info info = dockerClient.infoCmd().exec();
        AbstractDockerClientTest.LOG.info("Client info: {}", info.toString());

        int imgCount = info.getImages();
        AbstractDockerClientTest.LOG.info("imgCount1: {}", imgCount);

        // This should be an image that is not used by other repositories
        // already
        // pulled down, preferably small in size. If tag is not used pull will
        // download all images in that repository but tmpImgs will only
        // deleted 'latest' image but not images with other tags
        String testImage = "hackmann/empty";

        AbstractDockerClientTest.LOG.info("Removing image: {}", testImage);

        try {
            dockerClient.removeImageCmd(testImage).withForce().exec();
        } catch (NotFoundException e) {
            // just ignore if not exist
        }

        info = dockerClient.infoCmd().exec();
        AbstractDockerClientTest.LOG.info("Client info: {}", info.toString());

        imgCount = info.getImages();
        AbstractDockerClientTest.LOG.info("imgCount2: {}", imgCount);

        AbstractDockerClientTest.LOG.info("Pulling image: {}", testImage);

        dockerClient.pullImageCmd(testImage).exec(new PullImageResultCallback()).awaitSuccess();

        info = dockerClient.infoCmd().exec();
        AbstractDockerClientTest.LOG.info("Client info after pull, {}", info.toString());

        assertThat(imgCount, lessThanOrEqualTo(info.getImages()));

        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(testImage).exec();
        AbstractDockerClientTest.LOG.info("Image Inspect: {}", inspectImageResponse.toString());
        assertThat(inspectImageResponse, notNullValue());
    }

    @Test
    public void testPullNonExistingImage() throws Exception {

        // does not throw an exception
        // stream needs to be fully read in order to close the underlying connection
        dockerClient.pullImageCmd("xvxcv/foo").exec(new PullImageResultCallback()).awaitCompletion();
    }

}
