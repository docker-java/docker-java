package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;
import com.google.common.collect.ImmutableMap;
import org.hamcrest.Matcher;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.*;

@Test(groups = "integration")
public class ListServicesCmdExecTest extends AbstractNettyDockerClientTest {

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
    public void testListContainers() throws Exception {

        String testImage = "busybox";

        // // need to block until image is pulled completely
        // dockerClient.pullImageCmd(testImage).exec(new PullImageResultCallback()).awaitSuccess();

        List<Service> services = dockerClient.listServicesCmd().exec();
        assertThat(services, notNullValue());
        LOG.info("Service List: {}", services);

        int size = services.size();
    }

}
