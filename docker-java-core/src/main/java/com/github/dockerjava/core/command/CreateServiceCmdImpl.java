package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateServiceCmd;
import com.github.dockerjava.api.command.CreateServiceResponse;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.ServiceSpec;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Creates a new service
 */
public class CreateServiceCmdImpl extends AbstrDockerCmd<CreateServiceCmd, CreateServiceResponse> implements
        CreateServiceCmd {

    private ServiceSpec serviceSpec;

    private AuthConfig authConfig;

    public CreateServiceCmdImpl(CreateServiceCmd.Exec exec, ServiceSpec serviceSpec) {
        super(exec);
        checkNotNull(serviceSpec, "serviceSpec was not specified");
        withServiceSpec(serviceSpec);
    }

    @Override
    public ServiceSpec getServiceSpec() {
        return serviceSpec;
    }

    @Override
    public AuthConfig getAuthConfig() {
        return authConfig;
    }

    @Override
    public CreateServiceCmd withServiceSpec(ServiceSpec serviceSpec) {
        checkNotNull(serviceSpec, "serviceSpec was not specified");
        this.serviceSpec = serviceSpec;
        return this;
    }

    @Override
    public CreateServiceCmd withAuthConfig(AuthConfig authConfig) {
        checkNotNull(authConfig, "authConfig was not specified");
        this.authConfig = authConfig;
        return this;
    }
}
