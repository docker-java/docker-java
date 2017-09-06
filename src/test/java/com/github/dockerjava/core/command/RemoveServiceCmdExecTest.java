package com.github.dockerjava.core.command;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.api.model.TaskSpec;
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
import static org.hamcrest.Matchers.hasSize;

@Test(groups = "swarm-integration")
public class RemoveServiceCmdExecTest extends AbstractSwarmDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(RemoveServiceCmdExecTest.class);
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
    public void removeService() throws DockerException {
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

        dockerClient.removeServiceCmd(services.get(0).getId())
                .exec();

        services = dockerClient.listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(0));
    }

}
