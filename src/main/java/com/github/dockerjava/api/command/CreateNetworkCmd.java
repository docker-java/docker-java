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
    public String getName();

    @CheckForNull
    public String getDriver();

    @CheckForNull
    public Network.Ipam getIpam();

    /** The new network's name. Required. */
    public CreateNetworkCmd withName(@Nonnull String name);

    /** Optional custom IP scheme for the network. */
    public CreateNetworkCmd withIpamConfig(Ipam.Config config);

    /** Name of the network driver to use. Defaults to <code>bridge</code>. */
    public CreateNetworkCmd withDriver(String driver);

    /** Driver specific options */
    public CreateNetworkCmd withOptions(Map<String, String> options);

    public static interface Exec extends DockerCmdSyncExec<CreateNetworkCmd, CreateNetworkResponse> {
    }
}
