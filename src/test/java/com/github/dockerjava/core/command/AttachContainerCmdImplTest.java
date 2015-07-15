package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.io.File;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class AttachContainerCmdImplTest extends AbstractDockerClientTest {

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
    public void attachContainerWithoutTTY() throws Exception {

        String snippet = "hello world";

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("echo", snippet)
                .withTty(false).exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        CollectFramesCallback collectFramesCallback = new CollectFramesCallback() {
            @Override
            public void onNext(Frame frame) {
                assertEquals(frame.getStreamType(), StreamType.STDOUT);
                super.onNext(frame);
            };
        };

        dockerClient.attachContainerCmd(container.getId()).withStdErr().withStdOut()
                .withFollowStream().withLogs().exec(collectFramesCallback);

        collectFramesCallback.awaitCompletion(30, TimeUnit.SECONDS);

        collectFramesCallback.close();

        assertThat(collectFramesCallback.toString(), containsString(snippet));
    }

    @Test
    public void attachContainerWithTTY() throws Exception {

        File baseDir = new File(Thread.currentThread().getContextClassLoader()
                .getResource("attachContainerTestDockerfile").getFile());

        String imageId = buildImage(baseDir);

        CreateContainerResponse container = dockerClient.createContainerCmd(imageId).withTty(true).exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        CollectFramesCallback collectFramesCallback = new CollectFramesCallback() {
            @Override
            public void onNext(Frame frame) {
                assertEquals(frame.getStreamType(), StreamType.RAW);
                super.onNext(frame);
            };
        };

        dockerClient.attachContainerCmd(container.getId()).withStdErr().withStdOut()
                .withFollowStream().exec(collectFramesCallback);

        collectFramesCallback.awaitCompletion(15, TimeUnit.SECONDS);

        collectFramesCallback.close();

        System.out.println("log: " + collectFramesCallback.toString());

        //HexDump.dump(collectFramesCallback.toString().getBytes(), 0, System.out, 0);

        assertThat(collectFramesCallback.toString(), containsString("stdout\r\nstderr"));
    }
}
