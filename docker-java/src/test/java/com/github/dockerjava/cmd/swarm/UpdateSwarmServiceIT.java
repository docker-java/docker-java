package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.NetworkAttachmentConfig;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.ServiceModeConfig;
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.Task;
import com.github.dockerjava.api.model.TaskSpec;
import com.github.dockerjava.api.model.TaskState;
import com.github.dockerjava.junit.PrivateRegistryRule;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public class UpdateSwarmServiceIT extends SwarmCmdIT {
    @Test
    public void testUpdateServiceReplicate() {
        DockerClient dockerClient = startSwarm();
        //create network
        String networkId = dockerClient.createNetworkCmd().withName("networkname").withDriver("overlay")
                .withIpam(new Network.Ipam().withDriver("default")).exec().getId();
        TaskSpec taskSpec = new TaskSpec().withContainerSpec(
                new ContainerSpec().withImage("busybox").withArgs(Arrays.asList("sleep", "3600")).withInit(true));
        ServiceSpec serviceSpec = new ServiceSpec()
                .withMode(new ServiceModeConfig().withReplicated(new ServiceReplicatedModeOptions().withReplicas(1)))
                .withTaskTemplate(taskSpec)
                .withNetworks(Lists.newArrayList(new NetworkAttachmentConfig().withTarget(networkId)))
                .withName("worker");
        String serviceId = dockerClient.createServiceCmd(serviceSpec).exec().getId();
        await().untilAsserted(() -> {
            List<Service> services = dockerClient.listServicesCmd().withIdFilter(Arrays.asList(serviceId)).exec();
            assertThat(services.size(), is(1));
            Service service = services.get(0);
            ServiceSpec updateServiceSpec = service.getSpec()
                .withMode(new ServiceModeConfig().withReplicated(new ServiceReplicatedModeOptions().withReplicas(2)));
            dockerClient.updateServiceCmd(service.getId(), updateServiceSpec).withVersion(service.getVersion().getIndex()).exec();
            //verify the replicate
            Service updateService = dockerClient.listServicesCmd().withIdFilter(Arrays.asList(serviceId)).exec().get(0);
            assertThat(updateService.getSpec().getMode().getReplicated().getReplicas(), is(2L));
        });
    }

    @Test
    public void testUpdateServiceWithRegistryAuth() throws InterruptedException {
        DockerClient dockerClient = startSwarm();
        ServiceSpec serviceSpec = new ServiceSpec()
                .withName("authenticated-worker")
                .withMode(new ServiceModeConfig().withReplicated(new ServiceReplicatedModeOptions().withReplicas(1)))
                .withTaskTemplate(new TaskSpec().withContainerSpec(
                        new ContainerSpec().withImage(DEFAULT_IMAGE).withArgs(Arrays.asList("sleep", "3600"))));
        String serviceId = dockerClient.createServiceCmd(serviceSpec).exec().getId();

        await().atMost(60, TimeUnit.SECONDS).untilAsserted(() -> {
            List<Task> tasks = dockerClient.listTasksCmd()
                    .withServiceFilter(serviceId)
                    .withStateFilter(TaskState.RUNNING)
                    .exec();
            assertThat(tasks, hasSize(1));
        });

        try (PrivateRegistryRule registry = new PrivateRegistryRule(dockerClient)) {
            registry.start();
            String privateImage = registry.createPrivateImage("update-service");
            Service service = dockerClient.inspectServiceCmd(serviceId).exec();
            ServiceSpec updatedServiceSpec = service.getSpec();
            updatedServiceSpec.getTaskTemplate().getContainerSpec().withImage(privateImage);

            dockerClient.updateServiceCmd(serviceId, updatedServiceSpec)
                    .withVersion(service.getVersion().getIndex())
                    .withAuthConfig(registry.getAuthConfig())
                    .exec();

            await().atMost(60, TimeUnit.SECONDS).untilAsserted(() -> {
                List<Task> tasks = dockerClient.listTasksCmd()
                        .withServiceFilter(serviceId)
                        .withStateFilter(TaskState.RUNNING)
                        .exec();
                assertThat(tasks, hasSize(1));
                assertThat(tasks.get(0).getSpec().getContainerSpec().getImage(), startsWith(privateImage));
            });
        }
    }
}
