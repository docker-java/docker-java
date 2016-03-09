package com.github.dockerjava.api.command;

import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.Network.Ipam;
import com.github.dockerjava.core.RemoteApiVersion;

/**
 * Create a network.
 *
 * @since {@link RemoteApiVersion#VERSION_1_21}
 */
public interface CreateNetworkCmd extends SyncDockerCmd<CreateNetworkResponse> {

    @CheckForNull
    String getName();

    @CheckForNull
    String getDriver();

    @CheckForNull
    Network.Ipam getIpam();

    /** The new network's name. Required. */
    CreateNetworkCmd withName(@Nonnull String name);

    /** Optional custom IP scheme for the network. */
    CreateNetworkCmd withIpamConfig(Ipam.Config config);

    /** Name of the network driver to use. Defaults to <code>bridge</code>. */
    CreateNetworkCmd withDriver(String driver);

    /** Driver specific options */
    CreateNetworkCmd withOptions(Map<String, String> options);

    interface Exec extends DockerCmdSyncExec<CreateNetworkCmd, CreateNetworkResponse> {
    }
}
