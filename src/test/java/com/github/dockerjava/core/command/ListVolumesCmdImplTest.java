package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.command.ListVolumesResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class ListVolumesCmdImplTest extends AbstractDockerClientTest {

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
    public void listVolumes() throws DockerException {

        CreateVolumeResponse createVolumeResponse = dockerClient.createVolumeCmd().withName("volume1")
                .withDriver("local").exec();

        assertThat(createVolumeResponse.getName(), equalTo("volume1"));
        assertThat(createVolumeResponse.getDriver(), equalTo("local"));
        assertThat(createVolumeResponse.getMountpoint(), containsString("/volume1/"));

        ListVolumesResponse listVolumesResponse = dockerClient.listVolumesCmd().exec();

        assertThat(listVolumesResponse.getVolumes().size(), greaterThanOrEqualTo(1));
    }
}
