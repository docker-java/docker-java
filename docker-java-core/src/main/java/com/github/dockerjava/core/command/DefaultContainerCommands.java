package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AttachContainer;
import com.github.dockerjava.api.command.AttachContainerSpec;
import com.github.dockerjava.api.command.CreateContainer;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateContainerSpec;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.command.InspectContainer;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectContainerSpec;
import com.github.dockerjava.api.command.ListContainers;
import com.github.dockerjava.api.command.ListContainersResponse;
import com.github.dockerjava.api.command.ListContainersSpec;
import com.github.dockerjava.api.command.StartContainer;
import com.github.dockerjava.api.command.StartContainerSpec;
import com.github.dockerjava.api.command.StopContainer;
import com.github.dockerjava.api.command.StopContainerSpec;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;

import java.util.List;

public class DefaultContainerCommands implements DockerClient.ContainerCommands {

    final DockerCmdExecFactory dockerCmdExecFactory;

    public DefaultContainerCommands(DockerCmdExecFactory dockerCmdExecFactory) {
        this.dockerCmdExecFactory = dockerCmdExecFactory;
    }

    @Override
    public final CreateContainerResponse create(CreateContainer spec) {
        return new CreateContainerCmdImpl(
            dockerCmdExecFactory.createCreateContainerCmdExec(),
            CreateContainerSpec.copyOf(spec)
        ).exec();
    }

    @Override
    public final void start(StartContainer spec) {
        new StartContainerCmdImpl(
            dockerCmdExecFactory.createStartContainerCmdExec(),
            StartContainerSpec.copyOf(spec)
        ).exec();
    }

    @Override
    public InspectContainerResponse inspect(InspectContainer spec) {
        return new InspectContainerCmdImpl(
            dockerCmdExecFactory.createInspectContainerCmdExec(),
            InspectContainerSpec.copyOf(spec)
        ).exec();
    }

    @Override
    public ListContainersResponse list(ListContainers spec) {
        List<Container> containers = new ListContainersCmdImpl(
            dockerCmdExecFactory.createListContainersCmdExec(),
            ListContainersSpec.copyOf(spec)
        ).exec();
        return new ListContainersResponse(containers);
    }

    @Override
    public void stop(StopContainer spec) {
        new StopContainerCmdImpl(
            dockerCmdExecFactory.createStopContainerCmdExec(),
            StopContainerSpec.copyOf(spec)
        ).exec();
    }

    @Override
    public <T extends ResultCallback<Frame>> T attach(AttachContainer spec, T callback) {
        return new AttachContainerCmdImpl(
            dockerCmdExecFactory.createAttachContainerCmdExec(),
            AttachContainerSpec.copyOf(spec)
        ).exec(callback);
    }
}
