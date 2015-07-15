package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.core.CompressArchiveUtil;

@Test(groups = "integration")
public class BuildImageCmdImplTest extends AbstractDockerClientTest {

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
    public void testNginxDockerfileBuilder() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("nginx").getFile());

        String imageId = buildImage(baseDir);

        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageId).exec();
        assertThat(inspectImageResponse, not(nullValue()));
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());

        assertThat(inspectImageResponse.getAuthor(), equalTo("Guillaume J. Charmes \"guillaume@dotcloud.com\""));
    }

    @Test(groups = "ignoreInCircleCi")
    public void testNonstandard1() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader()
                .getResource("nonstandard/subdirectory/Dockerfile-nonstandard").getFile());

        buildImage(baseDir);
    }

    @Test(groups = "ignoreInCircleCi")
    public void testNonstandard2() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("nonstandard").getFile());
        File dockerFile = new File(Thread.currentThread().getContextClassLoader()
                .getResource("nonstandard/subdirectory/Dockerfile-nonstandard").getFile());

        dockerClient.buildImageCmd().withBaseDirectory(baseDir).withDockerfile(dockerFile)
                .withNoCache().exec(new BuildLogCallback()).awaitImageId();
    }

    @Test
    public void testDockerBuilderFromTar() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("testAddFile").getFile());
        Collection<File> files = FileUtils.listFiles(baseDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        File tarFile = CompressArchiveUtil.archiveTARFiles(baseDir, files, UUID.randomUUID().toString());
        String response = dockerfileBuild(new FileInputStream(tarFile));
        assertThat(response, containsString("Successfully executed testrun.sh"));
    }

    @Test
    public void testDockerBuilderAddUrl() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("testAddUrl").getFile());
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("Docker"));
    }

    @Test
    public void testDockerBuilderAddFileInSubfolder() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("testAddFileInSubfolder")
                .getFile());
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("Successfully executed testrun.sh"));
    }

    @Test
    public void testDockerBuilderAddFilesViaWildcard() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("testAddFilesViaWildcard")
                .getFile());
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("Successfully executed testinclude1.sh"));
        assertThat(response, not(containsString("Successfully executed testinclude2.sh")));
    }

    @Test
    public void testDockerBuilderAddFolder() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("testAddFolder").getFile());
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("Successfully executed testAddFolder.sh"));
    }

    @Test
    public void testDockerBuilderEnv() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("testEnv").getFile());
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("Successfully executed testrun.sh"));
    }

    private String dockerfileBuild(InputStream tarInputStream) throws Exception {

        return execBuild(dockerClient.buildImageCmd().withTarInputStream(tarInputStream));
    }

    private String dockerfileBuild(File baseDir) throws Exception {

        return execBuild(dockerClient.buildImageCmd(baseDir));
    }

    private String execBuild(BuildImageCmd buildImageCmd) throws Exception {

        BuildLogCallback resultCallback = new BuildLogCallback();;

        buildImageCmd.withNoCache().exec(resultCallback);

        String imageId = resultCallback.awaitImageId();

        // Create container based on image
        CreateContainerResponse container = dockerClient.createContainerCmd(imageId).exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();
        dockerClient.waitContainerCmd(container.getId()).exec();

        return containerLog(container.getId());

    }

    @Test(expectedExceptions = { DockerClientException.class })
    public void testDockerfileIgnored() throws Exception{
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("testDockerfileIgnored")
                .getFile());

        dockerClient.buildImageCmd(baseDir).withNoCache().exec(new BuildLogCallback()).awaitImageId();
    }

    @Test(expectedExceptions = { DockerClientException.class })
    public void testInvalidDockerIgnorePattern() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader()
                .getResource("testInvalidDockerignorePattern").getFile());

        dockerClient.buildImageCmd(baseDir).withNoCache().exec( new BuildLogCallback()).awaitImageId();
    }

    @Test(groups = "ignoreInCircleCi")
    public void testDockerIgnore() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("testDockerignore")
                .getFile());
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("/tmp/a/a /tmp/a/c /tmp/a/d"));
    }

    @Test
    public void testNetCatDockerfileBuilder() throws InterruptedException, IOException {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("netcat").getFile());

        BuildLogCallback resultCallback = new BuildLogCallback();
        dockerClient.buildImageCmd(baseDir).withNoCache().exec(resultCallback);

        resultCallback.awaitCompletion();

        String imageId = null;

        for (BuildResponseItem item : resultCallback.items) {
            String text = item.getStream();
            if (text.startsWith("Successfully built ")) {
                imageId = StringUtils.substringBetween(text, "Successfully built ", "\n").trim();
            }
        }

        assertNotNull(imageId, "Not successful in build");

        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageId).exec();
        assertThat(inspectImageResponse, not(nullValue()));
        assertThat(inspectImageResponse.getId(), not(nullValue()));
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());

        CreateContainerResponse container = dockerClient.createContainerCmd(inspectImageResponse.getId()).exec();
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getId(), notNullValue());
        assertThat(inspectContainerResponse.getNetworkSettings().getPorts(), notNullValue());

        // No use as such if not running on the server
        // for (Ports.Port p : inspectContainerResponse.getNetworkSettings().getPorts().getAllPorts()) {
        // int port = Integer.valueOf(p.getHostPort());
        // LOG.info("Checking port {} is open", port);
        // assertThat(available(port), is(false));
        // }
        dockerClient.stopContainerCmd(container.getId()).withTimeout(0).exec();

    }

    @Test
    public void testAddAndCopySubstitution() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("testENVSubstitution")
                .getFile());
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("testENVSubstitution successfully completed"));
    }
}
