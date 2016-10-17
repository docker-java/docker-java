package com.github.dockerjava.netty.exec;

import static com.github.dockerjava.utils.TestUtils.getVersion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import com.github.dockerjava.core.util.CompressArchiveUtil;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;

@Test(groups = "integration")
public class BuildImageCmdExecTest extends AbstractNettyDockerClientTest {

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
    public void author() throws Exception {

        String imageId = buildImage(fileFromBuildTestResource("AUTHOR"));

        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageId).exec();
        assertThat(inspectImageResponse, not(nullValue()));
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());

        assertThat(inspectImageResponse.getAuthor(), equalTo("Guillaume J. Charmes \"guillaume@dotcloud.com\""));
    }

    @Test
    public void buildImageFromTar() throws Exception {
        File baseDir = fileFromBuildTestResource("ADD/file");
        Collection<File> files = FileUtils.listFiles(baseDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        File tarFile = CompressArchiveUtil.archiveTARFiles(baseDir, files, UUID.randomUUID().toString());
        String response = dockerfileBuild(new FileInputStream(tarFile));
        assertThat(response, containsString("Successfully executed testrun.sh"));
    }

    @Test
    public void onBuild() throws Exception {
        File baseDir = fileFromBuildTestResource("ONBUILD/parent");

        dockerClient.buildImageCmd(baseDir).withNoCache(true).withTag("docker-java-onbuild")
                .exec(new BuildImageResultCallback()).awaitImageId();
        baseDir = fileFromBuildTestResource("ONBUILD/child");
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("Successfully executed testrun.sh"));
    }

    @Test
    public void addUrl() throws Exception {
        File baseDir = fileFromBuildTestResource("ADD/url");
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("Example Domain"));
    }

    @Test
    public void addFileInSubfolder() throws Exception {
        File baseDir = fileFromBuildTestResource("ADD/fileInSubfolder");
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("Successfully executed testrun.sh"));
    }

    @Test
    public void addFilesViaWildcard() throws Exception {
        File baseDir = fileFromBuildTestResource("ADD/filesViaWildcard");
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("Successfully executed testinclude1.sh"));
        assertThat(response, not(containsString("Successfully executed testinclude2.sh")));
    }

    @Test
    public void addFolder() throws Exception {
        File baseDir = fileFromBuildTestResource("ADD/folder");
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("Successfully executed testAddFolder.sh"));
    }

    private String dockerfileBuild(InputStream tarInputStream) throws Exception {

        return execBuild(dockerClient.buildImageCmd().withTarInputStream(tarInputStream));
    }

    private String dockerfileBuild(File baseDir) throws Exception {

        return execBuild(dockerClient.buildImageCmd(baseDir));
    }

    private String execBuild(BuildImageCmd buildImageCmd) throws Exception {
        String imageId = buildImageCmd.withNoCache(true).exec(new BuildImageResultCallback()).awaitImageId();

        // Create container based on image
        CreateContainerResponse container = dockerClient.createContainerCmd(imageId).exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();
        dockerClient.waitContainerCmd(container.getId()).exec(new WaitContainerResultCallback()).awaitStatusCode();

        return containerLog(container.getId());
    }

    @Test(expectedExceptions = {DockerClientException.class})
    public void dockerignoreDockerfileIgnored() throws Exception {
        File baseDir = fileFromBuildTestResource("dockerignore/DockerfileIgnored");

        dockerClient.buildImageCmd(baseDir).withNoCache(true).exec(new BuildImageResultCallback()).awaitImageId();
    }

    @Test
    public void dockerignoreDockerfileNotIgnored() throws Exception {
        File baseDir = fileFromBuildTestResource("dockerignore/DockerfileNotIgnored");

        dockerClient.buildImageCmd(baseDir).withNoCache(true).exec(new BuildImageResultCallback()).awaitImageId();
    }

    @Test(expectedExceptions = {DockerClientException.class})
    public void dockerignoreInvalidDockerIgnorePattern() throws Exception {
        File baseDir = fileFromBuildTestResource("dockerignore/InvalidDockerignorePattern");

        dockerClient.buildImageCmd(baseDir).withNoCache(true).exec(new BuildImageResultCallback()).awaitImageId();
    }

    @Test()
    public void dockerignoreValidDockerIgnorePattern() throws Exception {
        File baseDir = fileFromBuildTestResource("dockerignore/ValidDockerignorePattern");
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("/tmp/a/a /tmp/a/c /tmp/a/d"));
    }

    @Test
    public void env() throws Exception {
        File baseDir = fileFromBuildTestResource("ENV");
        String response = dockerfileBuild(baseDir);
        assertThat(response, containsString("testENVSubstitution successfully completed"));
    }

    @Test
    public void fromPrivateRegistry() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("privateRegistry").getFile());

        String imageId = buildImage(baseDir);

        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageId).exec();
        assertThat(inspectImageResponse, not(nullValue()));
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());

        dockerClient.tagImageCmd(imageId, "testregistry", "2").withForce().exec();

        // see https://github.com/docker/distribution/blob/master/docs/deploying.md#native-basic-auth
        CreateContainerResponse testregistry = dockerClient
                .createContainerCmd("testregistry:2")
                .withName("registry")
                .withPortBindings(new PortBinding(Binding.bindPort(5000), ExposedPort.tcp(5000)))
                .withEnv("REGISTRY_AUTH=htpasswd", "REGISTRY_AUTH_HTPASSWD_REALM=Registry Realm",
                        "REGISTRY_AUTH_HTPASSWD_PATH=/auth/htpasswd", "REGISTRY_LOG_LEVEL=debug",
                        "REGISTRY_HTTP_TLS_CERTIFICATE=/certs/domain.crt", "REGISTRY_HTTP_TLS_KEY=/certs/domain.key")
                .exec();

        dockerClient.startContainerCmd(testregistry.getId()).exec();

        // wait for registry to boot
        Thread.sleep(3000);

        // credentials as configured in /auth/htpasswd
        AuthConfig authConfig = new AuthConfig()
                .withUsername("testuser")
                .withPassword("testpassword")
                .withEmail("foo@bar.de")
                .withRegistryAddress("localhost:5000");

        dockerClient.authCmd().withAuthConfig(authConfig).exec();
        dockerClient.tagImageCmd("busybox:latest", "localhost:5000/testuser/busybox", "latest").withForce().exec();

        dockerClient.pushImageCmd("localhost:5000/testuser/busybox").withTag("latest").withAuthConfig(authConfig)
                .exec(new PushImageResultCallback()).awaitSuccess();

        dockerClient.removeImageCmd("localhost:5000/testuser/busybox").withForce(true).exec();

        baseDir = fileFromBuildTestResource("FROM/privateRegistry");

        AuthConfigurations authConfigurations = new AuthConfigurations();
        authConfigurations.addConfig(authConfig);

        imageId = dockerClient.buildImageCmd(baseDir).withNoCache(true).withBuildAuthConfigs(authConfigurations)
                .exec(new BuildImageResultCallback()).awaitImageId();

        inspectImageResponse = dockerClient.inspectImageCmd(imageId).exec();
        assertThat(inspectImageResponse, not(nullValue()));
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());

    }

    @Test
    public void buildArgs() throws Exception {
        File baseDir = fileFromBuildTestResource("buildArgs");

        String imageId = dockerClient.buildImageCmd(baseDir).withNoCache(true).withBuildArg("testArg", "abc")
                .exec(new BuildImageResultCallback())
                .awaitImageId();

        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageId).exec();
        assertThat(inspectImageResponse, not(nullValue()));
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());

        assertThat(inspectImageResponse.getConfig().getLabels().get("test"), equalTo("abc"));
    }

    @Test
    public void labels() throws Exception {
        if (!getVersion(dockerClient).isGreaterOrEqual(RemoteApiVersion.VERSION_1_23)) {
            throw new SkipException("API version should be >= 1.23");
        }

        File baseDir = fileFromBuildTestResource("labels");

        String imageId = dockerClient.buildImageCmd(baseDir).withNoCache(true)
                .withLabels(Collections.singletonMap("test", "abc"))
                .exec(new BuildImageResultCallback())
                .awaitImageId();

        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageId).exec();
        assertThat(inspectImageResponse, not(nullValue()));
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());

        assertThat(inspectImageResponse.getConfig().getLabels().get("test"), equalTo("abc"));
    }

    public void dockerfileNotInBaseDirectory() throws Exception {
        File baseDirectory = fileFromBuildTestResource("dockerfileNotInBaseDirectory");
        File dockerfile = fileFromBuildTestResource("dockerfileNotInBaseDirectory/dockerfileFolder/Dockerfile");
        BuildImageCmd command = dockerClient.buildImageCmd()
                .withBaseDirectory(baseDirectory)
                .withDockerfile(dockerfile);

        String response = execBuild(command);

        assertThat(response, containsString("Successfully executed testrun.sh"));
    }

    private File fileFromBuildTestResource(String resource) {
        return new File(Thread.currentThread().getContextClassLoader()
                .getResource("buildTests/" + resource).getFile());
    }
}
