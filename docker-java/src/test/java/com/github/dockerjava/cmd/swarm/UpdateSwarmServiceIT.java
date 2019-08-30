package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.NetworkAttachmentConfig;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.ServiceModeConfig;
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.api.model.TaskSpec;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UpdateSwarmServiceIT extends SwarmCmdIT {
    @Test
    public void testUpdateServiceReplicate() {
        DockerClient docker1 = startDockerInDocker();
        docker1.initializeSwarmCmd(new SwarmSpec()).exec();
        //create network
        String networkId = docker1.createNetworkCmd().withName("networkname").withDriver("overlay")
                .withIpam(new Network.Ipam().withDriver("default")).exec().getId();
        TaskSpec taskSpec = new TaskSpec().withContainerSpec(
                new ContainerSpec().withImage("busybox").withArgs(Arrays.asList("sleep", "3600")));
        ServiceSpec serviceSpec = new ServiceSpec()
                .withMode(new ServiceModeConfig().withReplicated(new ServiceReplicatedModeOptions().withReplicas(1)))
                .withTaskTemplate(taskSpec)
                .withNetworks(Lists.newArrayList(new NetworkAttachmentConfig().withTarget(networkId)))
                .withName("worker");
        String serviceId = docker1.createServiceCmd(serviceSpec).exec().getId();
        //list the service
        List<Service> services = docker1.listServicesCmd().withIdFilter(Arrays.asList(serviceId)).exec();
        assertThat(services.size(), is(1));
        Service service = services.get(0);
        ServiceSpec updateServiceSpec = service.getSpec()
                .withMode(new ServiceModeConfig().withReplicated(new ServiceReplicatedModeOptions().withReplicas(2)));
        docker1.updateServiceCmd(service.getId(), updateServiceSpec).withVersion(service.getVersion().getIndex()).exec();
        //verify the replicate
        Service updateService = docker1.listServicesCmd().withIdFilter(Arrays.asList(serviceId)).exec().get(0);
        assertThat(updateService.getSpec().getMode().getReplicated().getReplicas(), is(2L));
    }
}
