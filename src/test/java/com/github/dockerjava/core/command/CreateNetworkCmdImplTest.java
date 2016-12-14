package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.exception.DockerException;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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
    public void createNetworkWithLabels() throws DockerException {

        String networkName = "testNetwork";
        CreateNetworkResponse createNetworkResponse = dockerClient.createNetworkCmd()
            .withName(networkName).withLabels(Collections.singletonMap("test", "abc")).exec();

        assertNotNull(createNetworkResponse.getId());

        Network network = dockerClient.inspectNetworkCmd().withNetworkId(createNetworkResponse.getId()).exec();
        assertEquals(network.getName(), networkName);
        assertThat(network.getLabels().get("test"), equalTo("abc"));
        assertEquals(network.getDriver(), "bridge");
    }

}
