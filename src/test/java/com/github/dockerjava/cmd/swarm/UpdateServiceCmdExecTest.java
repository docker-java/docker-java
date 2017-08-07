package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.api.model.TaskSpec;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class UpdateServiceCmdExecTest extends SwarmCmdTest {

    public static final Logger LOG = LoggerFactory.getLogger(UpdateServiceCmdExecTest.class);
    private static final String SERVICE_NAME = "theservice";

    @Test
    public void updateService() throws DockerException {
        dockerRule.getClient().initializeSwarmCmd(new SwarmSpec()).withListenAddr("127.0.0.1").exec();

        ServiceSpec spec = new ServiceSpec()
                .withName(SERVICE_NAME)
                .withTaskTemplate(new TaskSpec()
                        .withContainerSpec(new ContainerSpec()
                                .withImage("busybox")));

        dockerRule.getClient().createServiceCmd(spec).exec();

        spec.getTaskTemplate().getContainerSpec().withImage("ubuntu");

        List<Service> services = dockerRule.getClient().listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));

        dockerRule.getClient().updateServiceCmd(services.get(0).getId(), spec)
                .withVersion(services.get(0).getVersion().getIndex())
                .exec();

        services = dockerRule.getClient().listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));

        assertThat(services.get(0).getSpec().getTaskTemplate().getContainerSpec().getImage(), is(equalTo("ubuntu")));

        dockerRule.getClient().removeServiceCmd(SERVICE_NAME);
    }

}
