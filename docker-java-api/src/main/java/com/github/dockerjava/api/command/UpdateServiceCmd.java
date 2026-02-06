package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.ServiceSpec;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public interface UpdateServiceCmd extends SyncDockerCmd<Void> {
    @CheckForNull
    String getServiceId();

    UpdateServiceCmd withServiceId(@Nonnull String serviceId);

    @CheckForNull
    ServiceSpec getServiceSpec();

    UpdateServiceCmd withServiceSpec(ServiceSpec serviceSpec);

    @CheckForNull
    Long getVersion();

    UpdateServiceCmd withVersion(Long version);

    @CheckForNull
    AuthConfig getAuthConfig();

    UpdateServiceCmd withAuthConfig(AuthConfig authConfig);

    interface Exec extends DockerCmdSyncExec<UpdateServiceCmd, Void> {
    }
}
