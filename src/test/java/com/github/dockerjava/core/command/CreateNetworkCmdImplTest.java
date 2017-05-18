package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.core.RemoteApiVersion;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.github.dockerjava.utils.TestUtils.getVersion;

@Test(groups = "integration")
public class CreateNetworkCmdImplTest extends AbstractDockerClientTest {

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

    @Test
    public void createNetworkWithIpamConfig() throws DockerException {

        String networkName = "testNetwork";
        Network.Ipam ipam = new Network.Ipam().withConfig(new Network.Ipam.Config().withSubnet("10.67.79.0/24"));
        CreateNetworkResponse createNetworkResponse = dockerClient.createNetworkCmd().withName(networkName).withIpam(ipam).exec();

        assertNotNull(createNetworkResponse.getId());

        Network network = dockerClient.inspectNetworkCmd().withNetworkId(createNetworkResponse.getId()).exec();
        assertEquals(network.getName(), networkName);
        assertEquals(network.getDriver(), "bridge");
        assertEquals("10.67.79.0/24", network.getIpam().getConfig().iterator().next().getSubnet());
    }

    @Test
    public void createAttachableNetwork() throws DockerException {
        final RemoteApiVersion apiVersion = getVersion(dockerClient);
        if (!apiVersion.isGreaterOrEqual(RemoteApiVersion.VERSION_1_24)) {
            throw new SkipException("API version should be >= 1.24");
        }
        String networkName = "createAttachableNetwork";
        CreateNetworkResponse createNetworkResponse = dockerClient.createNetworkCmd().withName(networkName).withAttachable(true).exec();
        assertNotNull(createNetworkResponse.getId());
        Network network = dockerClient.inspectNetworkCmd().withNetworkId(createNetworkResponse.getId()).exec();
        assertTrue(network.isAttachable());
    }

    @Test
    public void createNetworkWithLabel() throws DockerException {
        final RemoteApiVersion apiVersion = getVersion(dockerClient);
        if (!apiVersion.isGreaterOrEqual(RemoteApiVersion.VERSION_1_21)) {
            throw new SkipException("API version should be >= 1.21");
        }
        String networkName = "createNetworkWithLabel";
        Map<String,String> labels=new HashMap<>();
        labels.put("com.example.usage","test");
        CreateNetworkResponse createNetworkResponse = dockerClient.createNetworkCmd().withName(networkName).withLabels(labels).exec();
        assertNotNull(createNetworkResponse.getId());
        Network network = dockerClient.inspectNetworkCmd().withNetworkId(createNetworkResponse.getId()).exec();
        assertEquals(network.getLabels(), labels);
    }
}
