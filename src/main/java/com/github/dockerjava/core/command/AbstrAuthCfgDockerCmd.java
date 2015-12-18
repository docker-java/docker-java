package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.DockerCmd;
import com.github.dockerjava.api.command.DockerCmdSyncExec;
import com.github.dockerjava.api.model.AuthConfig;

public abstract class AbstrAuthCfgDockerCmd<T extends DockerCmd<RES_T>, RES_T> extends AbstrDockerCmd<T, RES_T> {

    public AbstrAuthCfgDockerCmd(DockerCmdSyncExec<T, RES_T> execution, AuthConfig authConfig) {
        super(execution);
        withOptionalAuthConfig(authConfig);
    }

    public AbstrAuthCfgDockerCmd(DockerCmdSyncExec<T, RES_T> execution) {
        super(execution);
    }

    private AuthConfig authConfig;

    public AuthConfig getAuthConfig() {
        return authConfig;
    }

    public T withAuthConfig(AuthConfig authConfig) {
        checkNotNull(authConfig, "authConfig was not specified");
        return withOptionalAuthConfig(authConfig);
    }

    @SuppressWarnings("unchecked")
    private T withOptionalAuthConfig(AuthConfig authConfig) {
        this.authConfig = authConfig;
        return (T) this;
    }

}
