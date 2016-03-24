package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

@Test(groups = "integration")
public class CreateNetworkCmdExecTest extends AbstractNettyDockerClientTest {

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
    public void createNetwork() throws DockerException {

        String networkName = "testNetwork";

        CreateNetworkResponse createNetworkResponse = dockerClient.createNetworkCmd().withName(networkName).exec();

        assertNotNull(createNetworkResponse.getId());

        Network network = dockerClient.inspectNetworkCmd().withNetworkId(createNetworkResponse.getId()).exec();
        assertEquals(network.getName(), networkName);
        assertEquals(network.getDriver(), "bridge");
    }
}
