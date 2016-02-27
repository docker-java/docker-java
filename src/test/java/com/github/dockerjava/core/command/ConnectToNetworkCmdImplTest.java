package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.client.AbstractDockerClientTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Collections;

@Test(groups = "integration")
public class ConnectToNetworkCmdImplTest extends AbstractDockerClientTest {

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
    public void connectToNetwork() throws InterruptedException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        dockerClient.startContainerCmd(container.getId()).exec();

        CreateNetworkResponse network = dockerClient.createNetworkCmd().withName("testNetwork").exec();

        dockerClient.connectToNetworkCmd().withNetworkId(network.getId()).withContainerId(container.getId()).exec();

        Network updatedNetwork = dockerClient.inspectNetworkCmd().withNetworkId(network.getId()).exec();

        assertTrue(updatedNetwork.getContainers().containsKey(container.getId()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertNotNull(inspectContainerResponse.getNetworkSettings().getNetworks().get("testNetwork"));
    }

    @Test
    public void connectToNetworkWithContainerNetwork() throws InterruptedException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        dockerClient.startContainerCmd(container.getId()).exec();

        CreateNetworkResponse network = dockerClient.createNetworkCmd()
                .withName("testNetwork")
                .withIpam(new Network.Ipam()
                    .withConfig(new Network.Ipam.Config()
                        .withSubnet("10.100.100.0/24")))
                .exec();

        dockerClient.connectToNetworkCmd()
                .withNetworkId(network.getId())
                .withContainerId(container.getId())
                .withContainerNetwork(new ContainerNetwork()
                    .withAliases("testing")
                    .withIpamConfig(new ContainerNetwork.Ipam()
                        .withIpv4Address("10.100.100.100")))
                .exec();

        Network updatedNetwork = dockerClient.inspectNetworkCmd().withNetworkId(network.getId()).exec();

        Network.ContainerNetworkConfig containerNetworkConfig = updatedNetwork.getContainers().get(container.getId());
        assertNotNull(containerNetworkConfig);
        assertEquals("10.100.100.100", containerNetworkConfig.getIpv4Address());

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        ContainerNetwork testNetwork = inspectContainerResponse.getNetworkSettings().getNetworks().get("testNetwork");
        assertNotNull(testNetwork);
        assertEquals(Collections.singletonList("testing"), testNetwork.getAliases());
        assertEquals("10.100.100.0", testNetwork.getGateway());
        assertEquals("10.100.100.100", testNetwork.getIpAddress());
    }
}
