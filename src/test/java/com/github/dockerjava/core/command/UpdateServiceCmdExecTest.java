package com.github.dockerjava.core.command;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.api.model.TaskSpec;
import com.github.dockerjava.core.command.swarm.AbstractSwarmDockerClientTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Test(groups = "swarm-integration")
public class UpdateServiceCmdExecTest extends AbstractSwarmDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(UpdateServiceCmdExecTest.class);
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
    public void updateService() throws DockerException {
        dockerClient.initializeSwarmCmd(new SwarmSpec()).withListenAddr("127.0.0.1").exec();

        ServiceSpec spec = new ServiceSpec()
                .withName(SERVICE_NAME)
                .withTaskTemplate(new TaskSpec()
                        .withContainerSpec(new ContainerSpec()
                                .withImage("busybox")));

        dockerClient.createServiceCmd(spec).exec();

        spec.getTaskTemplate().getContainerSpec().withImage("ubuntu");

        List<Service> services = dockerClient.listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));

        dockerClient.updateServiceCmd(services.get(0).getId(), spec)
                .withVersion(services.get(0).getVersion().getIndex())
                .exec();

        services = dockerClient.listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));

        assertThat(services.get(0).getSpec().getTaskTemplate().getContainerSpec().getImage(), is(equalTo("ubuntu")));

        dockerClient.removeServiceCmd(SERVICE_NAME);
    }

}
