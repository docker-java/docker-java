package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkResponse;
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
public class DisconnectFromNetworkCmdExecTest extends AbstractNettyDockerClientTest {

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
    public void disconnectFromNetwork() throws InterruptedException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        dockerClient.startContainerCmd(container.getId()).exec();

        CreateNetworkResponse network = dockerClient.createNetworkCmd().withName("testNetwork").exec();

        dockerClient.connectToNetworkCmd().withNetworkId(network.getId()).withContainerId(container.getId()).exec();

        Network updatedNetwork = dockerClient.inspectNetworkCmd().withNetworkId(network.getId()).exec();

        assertTrue(updatedNetwork.getContainers().containsKey(container.getId()));

        dockerClient.disconnectFromNetworkCmd().withNetworkId(network.getId()).withContainerId(container.getId()).exec();

        updatedNetwork = dockerClient.inspectNetworkCmd().withNetworkId(network.getId()).exec();

        assertFalse(updatedNetwork.getContainers().containsKey(container.getId()));
    }

    @Test
    public void forceDisconnectFromNetwork() throws InterruptedException {

        CreateNetworkResponse network = dockerClient.createNetworkCmd().withName("testNetwork").exec();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withNetworkMode("testNetwork")
                .withCmd("sleep", "9999")
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        dockerClient.disconnectFromNetworkCmd()
                .withNetworkId(network.getId())
                .withContainerId(container.getId())
                .withForce(true)
                .exec();

        Network updatedNetwork = dockerClient.inspectNetworkCmd().withNetworkId(network.getId()).exec();
        assertFalse(updatedNetwork.getContainers().containsKey(container.getId()));
    }
}
