package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.client.AbstractDockerClientTest;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Collections;

import static com.github.dockerjava.utils.TestUtils.isSwarm;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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
        if (isSwarm(dockerClient)) throw new SkipException("Swarm has no network");

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
        if (isSwarm(dockerClient)) throw new SkipException("Swarm has no network");

        final String NETWORK_SUBNET = "10.100.102.0/24";
        final String NETWORK_NAME = "jerseyTestNetwork";
        final String CONTAINER_IP = "10.100.102.100";

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withCmd("sleep", "9999")
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        try {
            dockerClient.removeNetworkCmd(NETWORK_NAME).exec();
        } catch (DockerException ignore) {
        }

        CreateNetworkResponse network = dockerClient.createNetworkCmd()
                .withName(NETWORK_NAME)
                .withIpam(new Network.Ipam()
                        .withConfig(new Network.Ipam.Config()
                                .withSubnet(NETWORK_SUBNET)))
                .exec();

        dockerClient.connectToNetworkCmd()
                .withNetworkId(network.getId())
                .withContainerId(container.getId())
                .withContainerNetwork(new ContainerNetwork()
                        .withAliases("aliasName")
                        .withIpamConfig(new ContainerNetwork.Ipam()
                                .withIpv4Address(CONTAINER_IP)))
                .exec();

        Network updatedNetwork = dockerClient.inspectNetworkCmd().withNetworkId(network.getId()).exec();

        Network.ContainerNetworkConfig containerNetworkConfig = updatedNetwork.getContainers().get(container.getId());
        assertNotNull(containerNetworkConfig);
        assertThat(containerNetworkConfig.getIpv4Address(), is(CONTAINER_IP + "/24"));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        ContainerNetwork testNetwork = inspectContainerResponse.getNetworkSettings().getNetworks().get(NETWORK_NAME);
        assertNotNull(testNetwork);
        assertThat(testNetwork.getAliases(), hasItem("aliasName"));
        assertEquals(testNetwork.getGateway(), "10.100.102.1");
        assertEquals(testNetwork.getIpAddress(), CONTAINER_IP);
    }
}
