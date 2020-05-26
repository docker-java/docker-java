package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.LogSwarmObjectCmd;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.ServiceModeConfig;
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions;
import com.github.dockerjava.api.model.ServiceRestartCondition;
import com.github.dockerjava.api.model.ServiceRestartPolicy;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.Task;
import com.github.dockerjava.api.model.TaskSpec;
import com.github.dockerjava.api.model.TaskState;
import com.github.dockerjava.utils.LogContainerTestCallback;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class LogSwarmObjectIT extends SwarmCmdIT {
    @Test
    public void testLogsCmd() throws InterruptedException, IOException {
        DockerClient dockerClient = startSwarm();
        String snippet = "hello world";
        TaskSpec taskSpec = new TaskSpec().withContainerSpec(
                new ContainerSpec().withImage("busybox").withCommand(Arrays.asList("echo", snippet)))
                .withRestartPolicy(new ServiceRestartPolicy().withCondition(ServiceRestartCondition.NONE));
        ServiceSpec serviceSpec = new ServiceSpec()
                .withMode(new ServiceModeConfig().withReplicated(new ServiceReplicatedModeOptions().withReplicas(1)))
                .withTaskTemplate(taskSpec)
                .withName("log-worker");
        String serviceId = dockerClient.createServiceCmd(serviceSpec).exec().getId();
        int since = (int) System.currentTimeMillis() / 1000;
        //wait the service to end
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tasks = dockerClient.listTasksCmd().withServiceFilter(serviceId).withStateFilter(TaskState.SHUTDOWN).exec();
            if (tasks.size() == 1) {
                break;
            } else {
                TimeUnit.SECONDS.sleep(3);
            }
        }
        assertThat(tasks.size(), is(1));
        String taskId = tasks.get(0).getId();
        //check service log
        validateLog(dockerClient.logServiceCmd(serviceId).withStdout(true), snippet);
        //check task log
        validateLog(dockerClient.logTaskCmd(taskId).withStdout(true), snippet);
        //check details/context
        // FIXME
        // validateLog(docker1.logServiceCmd(serviceId).withStdout(true).withDetails(true), "com.docker.swarm.service.id=" + serviceId);
        // validateLog(docker1.logTaskCmd(taskId).withStdout(true).withDetails(true), "com.docker.swarm.service.id=" + serviceId);
        //check since
        validateLog(dockerClient.logServiceCmd(serviceId).withStdout(true).withSince(since), snippet);
        validateLog(dockerClient.logTaskCmd(taskId).withStdout(true).withSince(since), snippet);
        dockerClient.removeServiceCmd(serviceId).exec();
    }

    private void validateLog(LogSwarmObjectCmd logCmd, String messsage) throws InterruptedException, IOException {
        try (LogContainerTestCallback loggingCallback = logCmd.exec(new LogContainerTestCallback(true))) {
            loggingCallback.awaitCompletion(5, TimeUnit.SECONDS);
            assertThat(loggingCallback.toString(), containsString(messsage));
        }
    }
}
