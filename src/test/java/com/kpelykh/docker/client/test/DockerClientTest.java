package com.kpelykh.docker.client.test;

import com.kpelykh.docker.client.DockerClient;
import com.kpelykh.docker.client.DockerClientException;
import com.kpelykh.docker.client.model.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

/**
 * Unit test for simple DockerClient.
 */
public class DockerClientTest extends Assert
{
    public static final Logger LOG = LoggerFactory.getLogger(DockerClientTest.class);

    private DockerClient dockerClient;

    private List<String> tmpImgs = new ArrayList<String>();
    private List<String> tmpContainers = new ArrayList<String>();

    @BeforeTest
    public void beforeTest() throws DockerClientException {
        LOG.info("======================= BEFORETEST =======================");
        LOG.info("Connecting to Docker server at http://localhost:4243");
        dockerClient = new DockerClient("http://localhost:4243");
        LOG.info("Creating image 'busybox'");

        InputStream in = null;
        try {
            in = dockerClient.pull("busybox");
        } finally {
            IOUtils.closeQuietly(in);
        }

        assertNotNull(dockerClient);
        LOG.info("======================= END OF BEFORETEST =======================\n\n");
    }

    @AfterTest
    public void afterTest() {
        LOG.info("======================= AFTERTEST =======================");

        for (String image : tmpImgs) {
            LOG.info("Cleaning up temporary image " + image);
            try {
                dockerClient.removeImage(image);
            } catch (DockerClientException ignore) {}
        }

        for (String container : tmpContainers) {
            LOG.info("Cleaning up temporary container " + container);
            try {
                dockerClient.removeContainer(container);
            } catch (DockerClientException ignore) {}
        }
        LOG.info("======================= END OF AFTERTEST =======================");
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        LOG.info(String.format("################################## STARTING %s ##################################", method.getName()));
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        LOG.info(String.format("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ END OF %s ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n", result.getName()));
    }

    /*
     * #########################
     * ## INFORMATION TESTS ##
     * #########################
    */

    @Test
    public void testDockerVersion() throws DockerClientException {
        Version version = dockerClient.version();
        LOG.info(version.toString());

        assertTrue(version.goVersion.length() > 0);
        assertTrue(version.version.length() > 0);

        assertEquals(StringUtils.split(version.version, ".").length, 3);

    }

    @Test
    public void testDockerInfo() throws DockerClientException {
        Info dockerInfo = dockerClient.info();
        LOG.info(dockerInfo.toString());

        assertTrue(dockerInfo.toString().contains("containers"));
        assertTrue(dockerInfo.toString().contains("images"));
        assertTrue(dockerInfo.toString().contains("debug"));

        assertFalse(dockerInfo.debug);
        assertTrue(dockerInfo.containers > 0);
        assertTrue(dockerInfo.images > 0);
        assertTrue(dockerInfo.NFd > 0);
        assertTrue(dockerInfo.NGoroutines > 0);
        assertTrue(dockerInfo.memoryLimit);
    }

    @Test
    public void testDockerSearch() throws DockerClientException {
        List<SearchItem> dockerSearch = dockerClient.search("busybox");
        LOG.info("Search returned" + dockerSearch.toString());

        Matcher matcher = hasItem(hasField("name", equalTo("busybox")));
        assertThat(dockerSearch, matcher);

        assertThat(filter(hasField("name", is("busybox")), dockerSearch).size(), equalTo(1));
    }

    /*
     * ###################
     * ## LISTING TESTS ##
     * ###################
     */


    @Test
    public void testImages() throws DockerClientException {
        List<Image> images = dockerClient.getImages(true);
        assertThat(images, notNullValue());
        LOG.info("Images List: " + images);
        Info info = dockerClient.info();

        assertEquals(images.size(), info.images);

        Image img = images.get(0);
        assertThat(img.created, is(greaterThan(0L)) );
        assertThat(img.size, is(greaterThan(0L)) );
        assertThat(img.virtualSize, is(greaterThan(0L)) );
        assertThat(img.id, not(isEmptyString()));
        assertThat(img.tag, not(isEmptyString()));
        assertThat(img.repository, not(isEmptyString()));
    }


    @Test
    public void testListContainers() throws DockerClientException {
        List<Container> containers = dockerClient.listContainers(true);
        assertThat(containers, notNullValue());
        LOG.info("Container List: " + containers);

        int size = containers.size();

        ContainerConfig containerConfig =
                new ContainerConfig.Builder("busybox")
                .cmd(new String[]{"echo"}).build();
        ContainerCreateResponse container1 = dockerClient.createContainer(containerConfig);
        assertThat(container1.id, not(isEmptyString()));
        dockerClient.startContainer(container1.id);
        tmpContainers.add(container1.id);

        List containers2 = dockerClient.listContainers(true);
        assertThat(size + 1, is(equalTo(containers2.size())));
        Matcher matcher = hasItem(hasField("id", startsWith(container1.id)));
        assertThat(containers2, matcher);

        List<Container> filteredContainers = filter(hasField("id", startsWith(container1.id)), containers2);
        assertThat(filteredContainers.size(), is(equalTo(1)));

        Container container2 = filteredContainers.get(0);
        assertThat(container2.command, not(isEmptyString()));
        assertThat(container2.image, equalTo("busybox:latest"));
    }


    /*
     * #####################
     * ## CONTAINER TESTS ##
     * #####################
     */

    @Test
    public void testCreateContainer() throws DockerClientException {
        ContainerConfig containerConfig =
                new ContainerConfig.Builder("busybox")
                        .cmd(new String[] {"true"}).build();

        ContainerCreateResponse container = dockerClient.createContainer(containerConfig);

        LOG.info("Created container " + container.toString());

        assertThat(container.id, not(isEmptyString()));

        tmpContainers.add(container.id);
    }

    @Test
    public void testStartContainer() throws DockerClientException {

        ContainerConfig containerConfig =
                new ContainerConfig.Builder("busybox")
                .cmd(new String[] {"true"}).build();

        ContainerCreateResponse container = dockerClient.createContainer(containerConfig);
        LOG.info("Created container " + container.toString());
        assertThat(container.id, not(isEmptyString()));
        tmpContainers.add(container.id);

        dockerClient.startContainer(container.id);

        ContainerInspectResponse containerInspectResponse = dockerClient.inspectContainer(container.id);
        LOG.info("Container Inspect: " + containerInspectResponse.toString());

        assertThat(containerInspectResponse.config, is(notNullValue()));
        assertThat(containerInspectResponse.id, not(isEmptyString()));

        assertThat(containerInspectResponse.id, startsWith(container.id));

        assertThat(containerInspectResponse.image, not(isEmptyString()));
        assertThat(containerInspectResponse.state, is(notNullValue()));

        assertThat(containerInspectResponse.state.running, is(true));

        if (!containerInspectResponse.state.running) {
            assertThat(containerInspectResponse.state.exitCode, is(equalTo(0)));
        }

    }

    @Test
    public void testWaitContainer() throws DockerClientException {

        ContainerConfig containerConfig =
                new ContainerConfig.Builder("busybox")
                        .cmd(new String[] {"true"}).build();

        ContainerCreateResponse container = dockerClient.createContainer(containerConfig);
        LOG.info("Created container " + container.toString());
        assertThat(container.id, not(isEmptyString()));
        tmpContainers.add(container.id);

        dockerClient.startContainer(container.id);

        int exitCode = dockerClient.waitContainer(container.id);
        LOG.info("Container exit code: " + exitCode);

        assertThat(exitCode, equalTo(0));

        ContainerInspectResponse containerInspectResponse = dockerClient.inspectContainer(container.id);
        LOG.info("Container Inspect: " + containerInspectResponse.toString());

        assertThat(containerInspectResponse.state.running, is(equalTo(false)));
        assertThat(containerInspectResponse.state.exitCode, is(equalTo(exitCode)));

    }

    @Test
    public void testLogs() throws DockerClientException {

        String snippet = "'Flowering Nights (Sakuya Iyazoi)";

        ContainerConfig containerConfig =
                new ContainerConfig.Builder("busybox")
                        .cmd(new String[] {"echo", "-n", snippet}).build();

        ContainerCreateResponse container = dockerClient.createContainer(containerConfig);
        LOG.info("Created container " + container.toString());
        assertThat(container.id, not(isEmptyString()));

        dockerClient.startContainer(container.id);
        tmpContainers.add(container.id);

        int exitCode = dockerClient.waitContainer(container.id);

        assertThat(exitCode, equalTo(0));

        String log = dockerClient.logContainer(container.id);
        LOG.info("Container log: " + log);

        assertThat(log, equalTo(snippet));
    }

    @Test
    public void testDiff() throws DockerClientException {
        ContainerConfig containerConfig =
                new ContainerConfig.Builder("busybox")
                        .cmd(new String[] {"touch", "/test"}).build();

        ContainerCreateResponse container = dockerClient.createContainer(containerConfig);
        LOG.info("Created container " + container.toString());
        assertThat(container.id, not(isEmptyString()));
        dockerClient.startContainer(container.id);
        tmpContainers.add(container.id);
        int exitCode = dockerClient.waitContainer(container.id);
        assertThat(exitCode, equalTo(0));

        List filesystemDiff = dockerClient.containterDiff(container.id);
        LOG.info("Container DIFF: " + filesystemDiff.toString());

        assertThat(filesystemDiff.size(), equalTo(4));
        ChangeLog testChangeLog = selectUnique(filesystemDiff, hasField("path", equalTo("/test")));

        assertThat(testChangeLog, hasField("path", equalTo("/test")));
        assertThat(testChangeLog, hasField("kind", equalTo(1)));
    }

    @Test
    public void testStopContainer() throws DockerClientException {

        ContainerConfig containerConfig =
                new ContainerConfig.Builder("busybox")
                        .cmd(new String[] {"sleep", "9999"}).build();
        ContainerCreateResponse container = dockerClient.createContainer(containerConfig);
        LOG.info("Created container " + container.toString());
        assertThat(container.id, not(isEmptyString()));
        dockerClient.startContainer(container.id);
        tmpContainers.add(container.id);

        LOG.info("Stopping container " + container.id);
        dockerClient.stopContainer(container.id, 2);

        ContainerInspectResponse containerInspectResponse = dockerClient.inspectContainer(container.id);
        LOG.info("Container Inspect:" + containerInspectResponse.toString());

        assertThat(containerInspectResponse.state.running, is(equalTo(false)));
        assertThat(containerInspectResponse.state.exitCode, not(equalTo(0)));
    }

    @Test
    public void testKillContainer() throws DockerClientException {

        ContainerConfig containerConfig =
                new ContainerConfig.Builder("busybox")
                        .cmd(new String[] {"sleep", "9999"}).build();
        ContainerCreateResponse container = dockerClient.createContainer(containerConfig);
        LOG.info("Created container " + container.toString());
        assertThat(container.id, not(isEmptyString()));
        dockerClient.startContainer(container.id);
        tmpContainers.add(container.id);

        LOG.info("Killing container " + container.id);
        dockerClient.kill(container.id);

        ContainerInspectResponse containerInspectResponse = dockerClient.inspectContainer(container.id);
        LOG.info("Container Inspect:" + containerInspectResponse.toString());

        assertThat(containerInspectResponse.state.running, is(equalTo(false)));
        assertThat(containerInspectResponse.state.exitCode, not(equalTo(0)));

    }

    @Test
    public void restartContainer() throws DockerClientException {

        ContainerConfig containerConfig =
                new ContainerConfig.Builder("busybox")
                        .cmd(new String[] {"sleep", "9999"}).build();
        ContainerCreateResponse container = dockerClient.createContainer(containerConfig);
        LOG.info("Created container " + container.toString());
        assertThat(container.id, not(isEmptyString()));
        dockerClient.startContainer(container.id);
        tmpContainers.add(container.id);

        ContainerInspectResponse containerInspectResponse = dockerClient.inspectContainer(container.id);
        LOG.info("Container Inspect:" + containerInspectResponse.toString());

        String startTime = containerInspectResponse.state.startedAt;

        dockerClient.restart(container.id, 2);

        ContainerInspectResponse containerInspectResponse2 = dockerClient.inspectContainer(container.id);
        LOG.info("Container Inspect After Restart:" + containerInspectResponse2.toString());

        String startTime2 = containerInspectResponse2.state.startedAt;

        assertThat(startTime, not(equalTo(startTime2)));

        assertThat(containerInspectResponse.state.running, is(equalTo(true)));

        dockerClient.kill(container.id);
    }

    @Test
    public void removeContainer() throws DockerClientException {

        ContainerConfig containerConfig =
                new ContainerConfig.Builder("busybox")
                        .cmd(new String[] {"true"}).build();

        ContainerCreateResponse container = dockerClient.createContainer(containerConfig);

        dockerClient.startContainer(container.id);
        dockerClient.waitContainer(container.id);
        tmpContainers.add(container.id);

        LOG.info("Removing container " + container.id);
        dockerClient.removeContainer(container.id);

        List containers2 = dockerClient.listContainers(true);
        Matcher matcher = not(hasItem(hasField("id", startsWith(container.id))));
        assertThat(containers2, matcher);

    }

    /*
     * ##################
     * ## IMAGES TESTS ##
     * ##################
     * */

    @Test
    public void testPullImage() throws DockerClientException {

        String testImage = "joffrey/test001";

        LOG.info("Removing image " + testImage);
        dockerClient.removeImage(testImage);

        Info info = dockerClient.info();
        LOG.info("Client info " + info.toString());

        long imgCount= info.images;

        LOG.info("Pulling image " + testImage);
        InputStream in = dockerClient.pull(testImage);

        byte[] buffer = new byte[1024];
        try {
            BufferedInputStream bis = new BufferedInputStream(in);
            int bytesRead = 0;
            while ((bytesRead = bis.read(buffer)) != -1) {
                Thread.sleep(200);
                String logChunk = new String(buffer, 0, bytesRead);
                LOG.info(logChunk);
            }
        } catch (IOException ex) {
        } catch (InterruptedException e) {
        } finally {
            IOUtils.closeQuietly(in);
        }

        tmpImgs.add(testImage);

        info = dockerClient.info();
        LOG.info("Client info after pull " + info.toString());

        assertThat(imgCount + 2, equalTo(info.images));

        ImageInspectResponse imageInspectResponse = dockerClient.inspectImage(testImage);
        LOG.info("Image Inspect: " + imageInspectResponse.toString());
        assertThat(imageInspectResponse, notNullValue());
    }


    @Test
    public void commitImage() throws DockerClientException {

        ContainerConfig containerConfig =
                new ContainerConfig.Builder("busybox")
                        .cmd(new String[] {"touch", "/test"}).build();

        ContainerCreateResponse container = dockerClient.createContainer(containerConfig);
        LOG.info("Created container " + container.toString());
        assertThat(container.id, not(isEmptyString()));
        dockerClient.startContainer(container.id);
        tmpContainers.add(container.id);

        LOG.info("Commiting container " + container.toString());
        String imageId = dockerClient.commit(new CommitConfig.Builder(container.id).build());
        tmpImgs.add(imageId);

        ImageInspectResponse imageInspectResponse = dockerClient.inspectImage(imageId);
        LOG.info("Image Inspect: " + imageInspectResponse.toString());

        assertThat(imageInspectResponse, hasField("container", startsWith(container.id)));
        assertThat(imageInspectResponse.containerConfig.image, equalTo("busybox"));

        ImageInspectResponse busyboxImg = dockerClient.inspectImage("busybox");

        assertThat(imageInspectResponse.parent, equalTo(busyboxImg.id));
    }

    @Test
    public void testRemoveImage() throws DockerClientException {

        ContainerConfig containerConfig =
                new ContainerConfig.Builder("busybox")
                        .cmd(new String[] {"touch", "/test"}).build();

        ContainerCreateResponse container = dockerClient.createContainer(containerConfig);
        LOG.info("Created container " + container.toString());
        assertThat(container.id, not(isEmptyString()));
        dockerClient.startContainer(container.id);
        tmpContainers.add(container.id);


        LOG.info("Commiting container " + container.toString());
        String imageId = dockerClient.commit(new CommitConfig.Builder(container.id).build());
        tmpImgs.add(imageId);

        LOG.info("Removing image" + imageId);
        dockerClient.removeImage(imageId);

        List containers = dockerClient.listContainers(true);
        Matcher matcher = not(hasItem(hasField("id", startsWith(imageId))));
        assertThat(containers, matcher);
    }


    /*
     *
     * ################
     * ## MISC TESTS ##
     * ################
     */

    @Test
    public void testRunShlex() throws DockerClientException {

        String[] commands = new String[] {
                "true",
                "echo \"The Young Descendant of Tepes & Septette for the Dead Princess\"",
                "echo -n 'The Young Descendant of Tepes & Septette for the Dead Princess'",
                "/bin/sh -c echo Hello World",
                "/bin/sh -c echo 'Hello World'",
                "echo 'Night of Nights'",
                "true && echo 'Night of Nights'"
        };

        for (String command : commands) {
            LOG.info("Running command [" + command + "]");

            ContainerConfig containerConfig =
                    new ContainerConfig.Builder("busybox")
                            .cmd( command ).build();
            ContainerCreateResponse container = dockerClient.createContainer(containerConfig);
            dockerClient.startContainer(container.id);
            tmpContainers.add(container.id);
            int exitcode = dockerClient.waitContainer(container.id);
            assertThat(exitcode, equalTo(0));
        }
    }

}