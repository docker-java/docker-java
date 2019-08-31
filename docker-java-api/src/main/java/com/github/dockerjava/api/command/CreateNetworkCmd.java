package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.Network.Ipam;
import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Map;

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

    @CheckForNull
    Map<String, String> getOptions();

    @CheckForNull
    Boolean getCheckDuplicate();

    @CheckForNull
    Boolean getInternal();

    @CheckForNull
    Boolean getEnableIPv6();

    @CheckForNull
    Boolean getAttachable();

    @CheckForNull
    Map<String, String> getLabels();

    /** The new network's name. Required. */
    CreateNetworkCmd withName(@Nonnull String name);

    /** Name of the network driver to use. Defaults to <code>bridge</code>. */
    CreateNetworkCmd withDriver(String driver);

    /** Ipam config, such es subnet, gateway and ip range of the network */
    CreateNetworkCmd withIpam(Ipam ipam);

    /** Driver specific options */
    CreateNetworkCmd withOptions(Map<String, String> options);

    CreateNetworkCmd withCheckDuplicate(boolean checkForDuplicate);

    CreateNetworkCmd withInternal(boolean internal);

    CreateNetworkCmd withEnableIpv6(boolean enableIpv6);

    /**
     * If enabled, and the network is in the global scope,  non-service containers on worker nodes will be able to connect to the network.
     *
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    CreateNetworkCmd withAttachable(Boolean attachable);

    /**
     * Add label for network
     *
     * @since {@link RemoteApiVersion#VERSION_1_24}
     */
    CreateNetworkCmd withLabels(Map<String, String> labels);

    interface Exec extends DockerCmdSyncExec<CreateNetworkCmd, CreateNetworkResponse> {
    }
}
