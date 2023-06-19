package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.RemoveSecretCmd;
import com.github.dockerjava.api.exception.NotFoundException;

import java.util.Objects;

/**
 * Remove a secret.
 */
public class RemoveSecretCmdImpl extends AbstrDockerCmd<RemoveSecretCmd, Void> implements RemoveSecretCmd {

    private String secretId;

    public RemoveSecretCmdImpl(Exec exec, String secretId) {
        super(exec);
        withSecretId(secretId);
    }

    @Override
    public String getSecretId() {
        return secretId;
    }

    @Override
    public RemoveSecretCmd withSecretId(String secretId) {
        this.secretId = Objects.requireNonNull(secretId, "secretId was not specified");
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such secret
     */
    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }
}
