package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Remove a container.
 *
 * @param removeVolumes
 *            - true or false, Remove the volumes associated to the container. Defaults to false
 * @param force
 *            - true or false, Removes the container even if it was running. Defaults to false
 */
public class RemoveContainerCmdImpl extends AbstrDockerCmd<RemoveContainerCmd, Void> implements RemoveContainerCmd {

    private String containerId;

    private Boolean removeVolumes, force;

    public RemoveContainerCmdImpl(RemoveContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public Boolean hasRemoveVolumesEnabled() {
        return removeVolumes;
    }

    @Override
    public Boolean hasForceEnabled() {
        return force;
    }

    @Override
    public RemoveContainerCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public RemoveContainerCmd withRemoveVolumes(Boolean removeVolumes) {
        this.removeVolumes = removeVolumes;
        return this;
    }

    @Override
    public RemoveContainerCmd withForce(Boolean force) {
        this.force = force;
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such container
     */

    //YD - use with force by default
    @Override
    public Void exec() throws NotFoundException {
        //YD - force removal of a running container when Jenkins master is done with it
        this.force = true;
        return super.exec();
    }
}
