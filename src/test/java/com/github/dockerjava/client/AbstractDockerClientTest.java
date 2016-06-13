package com.github.dockerjava.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse.Mount;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.TestDockerCmdExecFactory;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;

public abstract class AbstractDockerClientTest extends Assert {

    public static final Logger LOG = LoggerFactory.getLogger(AbstractDockerClientTest.class);

    protected DockerClient dockerClient;

    protected TestDockerCmdExecFactory dockerCmdExecFactory = initTestDockerCmdExecFactory();

    protected TestDockerCmdExecFactory initTestDockerCmdExecFactory() {
        return new TestDockerCmdExecFactory(
                DockerClientBuilder.getDefaultDockerCmdExecFactory());
    }

    public void beforeTest() throws Exception {

        LOG.info("======================= BEFORETEST =======================");
        LOG.info("Connecting to Docker server");
        dockerClient = DockerClientBuilder.getInstance(config())
                .withDockerCmdExecFactory(dockerCmdExecFactory)
                .build();

        try {
            dockerClient.inspectImageCmd("busybox").exec();
        } catch (NotFoundException e) {
            LOG.info("Pulling image 'busybox'");
            // need to block until image is pulled completely
            dockerClient.pullImageCmd("busybox").withTag("latest").exec(new PullImageResultCallback()).awaitSuccess();
        }

        assertNotNull(dockerClient);
        LOG.info("======================= END OF BEFORETEST =======================\n\n");
    }

    private DefaultDockerClientConfig config() {
        return config(null);
    }

    protected DefaultDockerClientConfig config(String password) {
        DefaultDockerClientConfig.Builder builder = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withRegistryUrl("https://index.docker.io/v1/");
        if (password != null) {
            builder = builder.withRegistryPassword(password);
        }

        return builder.build();
    }

    public void afterTest() {
        LOG.info("======================= END OF AFTERTEST =======================");
    }

    public void beforeMethod(Method method) {
        LOG.info(String.format("################################## STARTING %s ##################################",
                method.getName()));
    }

    public void afterMethod(ITestResult result) {

        for (String container : dockerCmdExecFactory.getContainerNames()) {
            LOG.info("Cleaning up temporary container {}", container);

            try {
                dockerClient.removeContainerCmd(container).withForce(true).exec();
            } catch (DockerException ignore) {
                // ignore.printStackTrace();
            }
        }

        for (String image : dockerCmdExecFactory.getImageNames()) {
            LOG.info("Cleaning up temporary image with {}", image);
            try {
                dockerClient.removeImageCmd(image).withForce(true).exec();
            } catch (DockerException ignore) {
                // ignore.printStackTrace();
            }
        }

        for (String volume : dockerCmdExecFactory.getVolumeNames()) {
            LOG.info("Cleaning up temporary volume with {}", volume);
            try {
                dockerClient.removeVolumeCmd(volume).exec();
            } catch (DockerException ignore) {
                // ignore.printStackTrace();
            }
        }

        for (String networkId : dockerCmdExecFactory.getNetworkIds()) {
            LOG.info("Cleaning up temporary network with {}", networkId);
            try {
                dockerClient.removeNetworkCmd(networkId).exec();
            } catch (DockerException ignore) {
                // ignore.printStackTrace();
            }
        }

        LOG.info("################################## END OF {} ##################################\n", result.getName());
    }

    protected String asString(InputStream response) {
        return consumeAsString(response);
    }

    public static String consumeAsString(InputStream response) {

        StringWriter logwriter = new StringWriter();

        try {
            LineIterator itr = IOUtils.lineIterator(response, "UTF-8");

            while (itr.hasNext()) {
                String line = itr.next();
                logwriter.write(line + (itr.hasNext() ? "\n" : ""));
                LOG.info("line: " + line);
            }
            response.close();

            return logwriter.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    // UTIL

    /**
     * Checks to see if a specific port is available.
     *
     * @param port
     *            the port to check for availability
     */
    public static Boolean available(int port) {
        if (port < 1100 || port > 60000) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException ignored) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    protected MountedVolumes mountedVolumes(Matcher<? super List<Volume>> subMatcher) {
        return new MountedVolumes(subMatcher, "Mounted volumes", "mountedVolumes");
    }

    private static class MountedVolumes extends FeatureMatcher<InspectContainerResponse, List<Volume>> {
        public MountedVolumes(Matcher<? super List<Volume>> subMatcher, String featureDescription, String featureName) {
            super(subMatcher, featureDescription, featureName);
        }

        @Override
        public List<Volume> featureValueOf(InspectContainerResponse item) {
            List<Volume> volumes = new ArrayList<Volume>();
            for (Mount mount : item.getMounts()) {
                volumes.add(mount.getDestination());
            }
            return volumes;
        }
    }

    protected String containerLog(String containerId) throws Exception {
        return dockerClient.logContainerCmd(containerId).withStdOut(true).exec(new LogContainerTestCallback())
                .awaitCompletion().toString();
    }

    public static class LogContainerTestCallback extends LogContainerResultCallback {
        protected final StringBuffer log = new StringBuffer();

        List<Frame> collectedFrames = new ArrayList<Frame>();

        boolean collectFrames = false;

        public LogContainerTestCallback() {
            this(false);
        }

        public LogContainerTestCallback(boolean collectFrames) {
            this.collectFrames = collectFrames;
        }

        @Override
        public void onNext(Frame frame) {
            if(collectFrames) collectedFrames.add(frame);
            log.append(new String(frame.getPayload()));
        }

        @Override
        public String toString() {
            return log.toString();
        }


        public List<Frame> getCollectedFrames() {
            return collectedFrames;
        }
    }

    protected String buildImage(File baseDir) throws Exception {

        return dockerClient.buildImageCmd(baseDir).withNoCache(true).exec(new BuildImageResultCallback())
                .awaitImageId();
    }

    protected Network findNetwork(List<Network> networks, String name) {

        for (Network network : networks) {
            if (StringUtils.equals(network.getName(), name)) {
                return network;
            }
        }

        fail("No network found.");
        return null;
    }

}
