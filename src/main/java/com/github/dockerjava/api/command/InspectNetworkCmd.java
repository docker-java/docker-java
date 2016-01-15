package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Network;

import javax.annotation.CheckForNull;

/**
 * Available since API v1.21.
 */
public interface InspectNetworkCmd extends SyncDockerCmd<Network> {

    @CheckForNull
    public String getNetworkId();

    public InspectNetworkCmd withNetworkId(String networkId);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public Network exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<InspectNetworkCmd, Network> {
    }
}
