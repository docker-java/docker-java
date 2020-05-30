package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateServiceResponse;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.ServiceModeConfig;
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.TaskSpec;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class ListServicesCmdExecIT extends SwarmCmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(CreateServiceCmdExecIT.class);
    private static final String SERVICE_NAME = "inspect_service";
    private static final String LABEL_KEY = "com.github.dockerjava.usage";
    private static final String LABEL_VALUE = "test";

    @Test
    public void testListServices() throws Exception {
        DockerClient dockerClient = startSwarm();
        Map<String, String> serviceLabels = Collections.singletonMap(LABEL_KEY, LABEL_VALUE);
        CreateServiceResponse response = dockerClient.createServiceCmd(new ServiceSpec()
                .withLabels(serviceLabels)
                .withName(SERVICE_NAME)
                .withMode(new ServiceModeConfig().withReplicated(
                        new ServiceReplicatedModeOptions()
                                .withReplicas(1)
                ))
                .withTaskTemplate(new TaskSpec()
                        .withContainerSpec(new ContainerSpec()
                                .withImage(DEFAULT_IMAGE))))
                .exec();
        String serviceId = response.getId();
        //filtering with service id
        List<Service> services = dockerClient.listServicesCmd().withIdFilter(Collections.singletonList(serviceId)).exec();
        assertThat(services, hasSize(1));
        //filtering with service name
        services = dockerClient.listServicesCmd().withNameFilter(Collections.singletonList(SERVICE_NAME)).exec();
        assertThat(services, hasSize(1));
        //filter labels
        services = dockerClient.listServicesCmd().withLabelFilter(serviceLabels).exec();
        assertThat(services, hasSize(1));
        dockerClient.removeServiceCmd(SERVICE_NAME).exec();
    }
}
