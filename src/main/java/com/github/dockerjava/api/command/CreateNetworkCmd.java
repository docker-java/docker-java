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

    @CheckForNull
    public Map<String, String> getOptions();

    @CheckForNull
    public Boolean getCheckDuplicate();

    @CheckForNull
    public Boolean getInternal();

    @CheckForNull
    public Boolean getEnableIPv6();

    /** The new network's name. Required. */
    public CreateNetworkCmd withName(@Nonnull String name);

    /** Name of the network driver to use. Defaults to <code>bridge</code>. */
    public CreateNetworkCmd withDriver(String driver);

    /** Ipam config, such es subnet, gateway and ip range of the network */
    CreateNetworkCmd withIpam(Ipam ipam);

    /** Driver specific options */
    public CreateNetworkCmd withOptions(Map<String, String> options);

    public CreateNetworkCmd withCheckDuplicate(boolean checkForDuplicate);

    public CreateNetworkCmd withInternal(boolean internal);

    public CreateNetworkCmd withEnableIpv6(boolean enableIpv6);

    public static interface Exec extends DockerCmdSyncExec<CreateNetworkCmd, CreateNetworkResponse> {
    }
}
