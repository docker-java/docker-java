package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.client.AbstractDockerClientTest;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

@Test(groups = "integration")
public class RemoveNetworkCmdImplTest extends AbstractDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(RemoveNetworkCmdImplTest.class);

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

    @Test(groups = "ignoreInCircleCi")
    public void removeNetwork() throws DockerException {

        CreateNetworkResponse network = dockerClient.createNetworkCmd().withName("test-network").exec();

        LOG.info("Removing network: {}", network.getId());
        dockerClient.removeNetworkCmd(network.getId()).exec();

        List<Network> networks = dockerClient.listNetworksCmd().exec();

        Matcher matcher = not(hasItem(hasField("id", startsWith(network.getId()))));
        assertThat(networks, matcher);

    }

    @Test(expectedExceptions = NotFoundException.class)
    public void removeNonExistingContainer() throws DockerException {

        dockerClient.removeNetworkCmd("non-existing").exec();
    }

}
