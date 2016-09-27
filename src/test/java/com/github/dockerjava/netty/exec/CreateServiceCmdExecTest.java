package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.NetworkAttachmentConfig;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.ServiceModeConfig;
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.api.model.TaskSpec;
import com.github.dockerjava.netty.AbstractNettySwarmDockerClientTest;
import com.google.common.collect.Lists;
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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Test(groups = "swarm-integration")
public class CreateServiceCmdExecTest extends AbstractNettySwarmDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(CreateServiceCmdExecTest.class);
    private static final String SERVICE_NAME = "theservice";

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
    public void testCreateService() throws DockerException {
        dockerClient.initializeSwarmCmd(new SwarmSpec())
                .withListenAddr("127.0.0.1")
                .withAdvertiseAddr("127.0.0.1")
                .exec();

        dockerClient.createServiceCmd(new ServiceSpec()
                .withName(SERVICE_NAME)
                .withTaskTemplate(new TaskSpec()
                        .withContainerSpec(new ContainerSpec()
                                .withImage("busybox"))))
        .exec();

        List<Service> services = dockerClient.listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));

        dockerClient.removeServiceCmd(SERVICE_NAME).exec();
    }

    @Test
    public void testCreateServiceWithNetworks() {
        dockerClient.initializeSwarmCmd(new SwarmSpec())
                .withListenAddr("127.0.0.1")
                .withAdvertiseAddr("127.0.0.1")
                .exec();

        String networkId = dockerClient.createNetworkCmd().withName("networkname")
                .withDriver("overlay")
                .withIpam(new Network.Ipam()
                        .withDriver("default"))
                .exec().getId();

        ServiceSpec spec = new ServiceSpec()
                .withName(SERVICE_NAME)
                .withTaskTemplate(new TaskSpec()
                        .withContainerSpec(new ContainerSpec()
                                .withImage("busybox"))
                        )
                .withNetworks(Lists.newArrayList(
                        new NetworkAttachmentConfig()
                                .withTarget(networkId)
                                .withAliases(Lists.<String>newArrayList("alias1", "alias2"))
                ))
                .withMode(new ServiceModeConfig().withReplicated(
                        new ServiceReplicatedModeOptions()
                                .withReplicas(1)
                ));

        dockerClient.createServiceCmd(spec).exec();

        List<Service> services = dockerClient.listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));

        assertThat(services.get(0).getSpec(), is(spec));

        dockerClient.removeServiceCmd(SERVICE_NAME).exec();

    }

}
