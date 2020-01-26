package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectContainerSpec;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Inspect the details of a container.
 */
public class InspectContainerCmdImpl extends AbstrDockerCmd<InspectContainerCmd, InspectContainerResponse> implements
        InspectContainerCmd {

    private InspectContainerSpec spec;

    public InspectContainerCmdImpl(InspectContainerCmd.Exec exec, String containerId) {
        super(exec);

        this.spec = InspectContainerSpec.builder()
                .containerId(containerId)
                .build();
    }

    @Override
    public InspectContainerCmd fromSpec(InspectContainerSpec spec) {
        this.spec = spec;
        return this;
    }

    @Override
    public String getContainerId() {
        return spec.getContainerId();
    }

    @Override
    public InspectContainerCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.spec = spec.withContainerId(containerId);
        return this;
    }

    @Override
    public InspectContainerCmd withSize(Boolean showSize) {
        this.spec = spec.withSize(showSize);
        return this;
    }

    @Override
    public Boolean getSize() {
        return spec.getSize();
    }

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public InspectContainerResponse exec() throws NotFoundException {
        return super.exec();
    }
}
