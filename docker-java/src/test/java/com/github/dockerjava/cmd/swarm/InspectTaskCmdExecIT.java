package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.CreateServiceResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.NetworkAttachment;
import com.github.dockerjava.api.model.NetworkAttachmentConfig;
import com.github.dockerjava.api.model.ServiceModeConfig;
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.Task;
import com.github.dockerjava.api.model.TaskSpec;
import com.github.dockerjava.api.model.TaskState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class InspectTaskCmdExecIT extends SwarmCmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(InspectTaskCmdExecIT.class);
    private static final String SERVICE_NAME = "inspect_task";
    private static final String TASK_LABEL_KEY = "com.github.dockerjava.usage";
    private static final String TASK_LABEL_VALUE = "test";
    public static final String NETWORK_NAME = "testnet";

    private DockerClient dockerClient;


    @Before
    public void startUp() {
        this.dockerClient = startSwarm();
    }

    @After
    public void tearDown() {
        try {
            this.dockerClient.close();

        } catch (IOException e) {
            LOG.error("Failed to close connection to docker: ", e);
        }
    }


    @Test
    public void testInspectTask() throws DockerException {
        ServiceSpec serviceSpec = getDefaultSpec();
        long serviceCreateTime = System.currentTimeMillis();
        CreateServiceResponse serviceResponse = createService(dockerClient, serviceSpec);
        Task task = getTaskFromSwarm(serviceResponse.getId());

        // Basic information
        assertThat(task.getId(), notNullValue());
        assertThat(task.getCreatedAt().getTime() - serviceCreateTime, lessThan(10000L));
        assertThat(task.getUpdatedAt().getTime() - serviceCreateTime, lessThan(10000L));
        assertThat(task.getServiceId(), is(serviceResponse.getId()));
        assertThat(task.getNodeId(), notNullValue());
        assertThat(task.getSlot(), is(1));
        assertThat(task.getStatus(), notNullValue());
        assertThat(task.getDesiredState(), is(TaskState.RUNNING));
        assertThat(task.getNetworkAttachments(), nullValue());
    }

    @Test
    public void testInspectTaskNetwork() throws DockerException {
        CreateNetworkResponse networkResponse = dockerClient.createNetworkCmd()
            .withName(NETWORK_NAME)
            .withDriver("overlay")
            .exec();

        ServiceSpec serviceSpec = getDefaultSpec()
            .withNetworks(Collections.singletonList(new NetworkAttachmentConfig()
                .withTarget(NETWORK_NAME)));
        CreateServiceResponse serviceResponse = createService(dockerClient, serviceSpec);
        Task task = getTaskFromSwarm(serviceResponse.getId());

        assertThat(task.getNetworkAttachments(), notNullValue());
        assertThat(task.getNetworkAttachments().size(), is(1));

        NetworkAttachment networkAttachment = task.getNetworkAttachments().get(0);
        assertThat(networkAttachment.getAddresses(), notNullValue());
        assertThat(networkAttachment.getAddresses().size(), is(1));

        NetworkAttachment.Network network = networkAttachment.getNetwork();
        assertThat(network, notNullValue());
        assertThat(network.getId(), is(networkResponse.getId()));
        assertThat(network.getDriverState(), notNullValue());
        assertThat(network.getDriverState().getName(), is("overlay"));

        NetworkAttachment.Network.Spec spec = network.getSpec();
        assertThat(spec, notNullValue());
        assertThat(spec.getName(), is(NETWORK_NAME));
        assertThat(spec.getScope(), is("swarm"));

    }


    /**
     * Creates the default version of the service spec
     *
     * @return The default ServiceSpec
     */
    private ServiceSpec getDefaultSpec() {
        return new ServiceSpec()
            .withName(SERVICE_NAME)
            .withMode(new ServiceModeConfig()
                .withReplicated(new ServiceReplicatedModeOptions()
                    .withReplicas(1)
                )
            ).withTaskTemplate(new TaskSpec()
                .withContainerSpec(new ContainerSpec()
                    .withImage(DEFAULT_IMAGE)))
            ;
    }

    /**
     * Utility method to quickly create a service and wait for its tasks to be up and running
     *
     * @param dockerClient The docker client to create the service in
     * @param serviceSpec  The ServiceSpec to create the service with
     * @return The creation response
     */
    private CreateServiceResponse createService(DockerClient dockerClient, ServiceSpec serviceSpec) {
        CreateServiceResponse response = dockerClient.createServiceCmd(serviceSpec)
            .exec();

        String serviceId = response.getId();
        await().until(
            () -> dockerClient.listTasksCmd().withServiceFilter(serviceId).exec(),
            hasSize(1)
        );

        return response;
    }

    /**
     * Returns the first task from a service id
     *
     * @param serviceID The ID of the service
     * @return The first task of the service
     */
    private Task getTaskFromSwarm(String serviceID) {
        String taskID = dockerClient.listTasksCmd()
            .withServiceFilter(serviceID)
            .exec().get(0).getId();

        return dockerClient.inspectTaskCmd(taskID)
            .exec();
    }

}
