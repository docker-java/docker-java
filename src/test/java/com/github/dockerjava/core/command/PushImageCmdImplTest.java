package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class PushImageCmdImplTest extends AbstractDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(PushImageCmdImplTest.class);

    String username;

    @BeforeTest
    public void beforeTest() throws Exception {
        super.beforeTest();
        username = dockerClient.authConfig().getUsername();
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
    public void pushLatest() throws Exception {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("true").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        LOG.info("Committing container: {}", container.toString());
        String imageId = dockerClient.commitCmd(container.getId()).withRepository(username + "/busybox").exec();


        PushResponseCallback callback  = new PushResponseCallback();

        // we have to block until image is pushed
        dockerClient.pushImageCmd(username + "/busybox", callback).exec();

        callback.awaitFinish();

        LOG.info("Removing image: {}", imageId);
        dockerClient.removeImageCmd(imageId).exec();

        PullResponseCallback pushCallback = new PullResponseCallback();

        dockerClient.pullImageCmd(username + "/busybox", pushCallback).exec();

        pushCallback.awaitFinish();

        assertThat(pushCallback.toString(), not(containsString("HTTP code: 404")));
    }

    @Test
    public void pushNonExistentImage() throws Exception {

        PushResponseCallback callback  = new PushResponseCallback();

        dockerClient.pushImageCmd(username + "/xxx", callback).exec();

        callback.awaitFinish();

        assertThat(callback.toString(), containsString("error"));
    }

    public static class PushResponseCallback extends CollectStreamItemCallback<PushResponseItem> {

    }

}
