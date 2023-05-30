package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateSecretCmd;
import com.github.dockerjava.api.command.CreateSecretResponse;
import com.github.dockerjava.api.model.SecretSpec;

import java.util.Objects;

/**
 * Creates a new secret
 */
public class CreateSecretCmdImpl extends AbstrDockerCmd<CreateSecretCmd, CreateSecretResponse> implements
        CreateSecretCmd {

    private SecretSpec secretSpec;

    public CreateSecretCmdImpl(Exec exec, SecretSpec secretSpec) {
        super(exec);
        Objects.requireNonNull(secretSpec, "secretSpec was not specified");
        withSecretSpec(secretSpec);
    }

    @Override
    public SecretSpec getSecretSpec() {
        return secretSpec;
    }

    @Override
    public CreateSecretCmd withSecretSpec(SecretSpec secretSpec) {
        Objects.requireNonNull(secretSpec, "secretSpec was not specified");
        this.secretSpec = secretSpec;
        return this;
    }
}
