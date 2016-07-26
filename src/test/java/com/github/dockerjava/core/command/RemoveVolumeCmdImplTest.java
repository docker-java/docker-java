package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.client.AbstractDockerClientTest;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@Test(groups = "integration")
public class RemoveVolumeCmdImplTest extends AbstractDockerClientTest {

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

    @Test(expectedExceptions = NotFoundException.class)
    public void removeVolume() throws DockerException {

        String volumeName = "volume1";

        CreateVolumeResponse createVolumeResponse = dockerClient.createVolumeCmd().withName(volumeName)
                .withDriver("local").exec();

        assertThat(createVolumeResponse.getName(), equalTo(volumeName));
        assertThat(createVolumeResponse.getDriver(), equalTo("local"));
        assertThat(createVolumeResponse.getMountpoint(), containsString("/volume1/"));

        dockerClient.removeVolumeCmd(volumeName).exec();

        dockerClient.inspectVolumeCmd(volumeName).exec();
    }
}
