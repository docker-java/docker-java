package com.github.dockerjava.core.command;

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
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Test(groups = "integration")
public class InspectNetworkCmdImplTest extends AbstractDockerClientTest {

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
    public void inspectNetwork() throws DockerException {

        List<Network> networks = dockerClient.listNetworksCmd().exec();

        Network expected = findNetwork(networks, "bridge");

        Network network = dockerClient.inspectNetworkCmd().withNetworkId(expected.getId()).exec();

        assertThat(network.getName(), equalTo(expected.getName()));
        assertThat(network.getScope(), equalTo(expected.getScope()));
        assertThat(network.getDriver(), equalTo(expected.getDriver()));
        assertThat(network.getIpam().getConfig().get(0).getSubnet(), equalTo(expected.getIpam().getConfig().get(0).getSubnet()));
        assertThat(network.getIpam().getDriver(), equalTo(expected.getIpam().getDriver()));
    }
}
