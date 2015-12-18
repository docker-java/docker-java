package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class CreateVolumeCmdImplTest extends AbstractDockerClientTest {

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
    public void createVolume() throws DockerException {

        String volumeName = "volume1";

        CreateVolumeResponse createVolumeResponse = dockerClient.createVolumeCmd().withName(volumeName)
                .withDriver("local").exec();

        assertThat(createVolumeResponse.getName(), equalTo(volumeName));
        assertThat(createVolumeResponse.getDriver(), equalTo("local"));
        assertThat(createVolumeResponse.getMountpoint(), containsString("/volume1/"));
    }

    @Test
    public void createVolumeWithExistingName() throws DockerException {

        String volumeName = "volume1";

        CreateVolumeResponse createVolumeResponse1 = dockerClient.createVolumeCmd().withName(volumeName)
                .withDriver("local").exec();

        assertThat(createVolumeResponse1.getName(), equalTo(volumeName));
        assertThat(createVolumeResponse1.getDriver(), equalTo("local"));
        assertThat(createVolumeResponse1.getMountpoint(), containsString("/volume1/"));

        CreateVolumeResponse createVolumeResponse2 = dockerClient.createVolumeCmd().withName(volumeName)
                .withDriver("local").exec();

        assertThat(createVolumeResponse2.getName(), equalTo(volumeName));
        assertThat(createVolumeResponse2.getDriver(), equalTo("local"));
        assertThat(createVolumeResponse2.getMountpoint(), equalTo(createVolumeResponse1.getMountpoint()));
    }
}
