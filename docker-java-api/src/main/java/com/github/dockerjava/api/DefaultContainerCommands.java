package com.github.dockerjava.api;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AttachContainerSpec;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateContainerSpec;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectContainerSpec;
import com.github.dockerjava.api.command.ListContainersResponse;
import com.github.dockerjava.api.command.ListContainersSpec;
import com.github.dockerjava.api.command.StartContainerSpec;
import com.github.dockerjava.api.command.StopContainerSpec;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;

import java.util.List;

class DefaultContainerCommands implements DockerClient.ContainerCommands {

    final DockerClient dockerClient;

    DefaultContainerCommands(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Override
    public CreateContainerResponse create(CreateContainerSpec spec) {
        return dockerClient.createContainerCmd(spec.getImage())
                .fromSpec(spec)
                .exec();
    }

    @Override
    public InspectContainerResponse inspect(InspectContainerSpec spec) {
        return dockerClient.inspectContainerCmd(spec.getContainerId())
                .fromSpec(spec)
                .exec();
    }

    @Override
    public ListContainersResponse list(ListContainersSpec request) {
        List<Container> containers = dockerClient.listContainersCmd()
                .fromSpec(request)
                .exec();
        return new ListContainersResponse(containers);
    }

    @Override
    public void start(StartContainerSpec request) {
        dockerClient.startContainerCmd(request.getContainerId())
                .fromSpec(request)
                .exec();
    }

    @Override
    public void stop(StopContainerSpec request) {
        dockerClient.stopContainerCmd(request.getContainerId())
                .fromSpec(request)
                .exec();
    }

    @Override
    public void attach(AttachContainerSpec request, ResultCallback<Frame> callback) {
        dockerClient.attachContainerCmd(request.getContainerId())
                .fromSpec(request)
                .exec(callback);
    }
}
