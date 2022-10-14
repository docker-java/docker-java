package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PushResponseItem;

/**
 * Push the latest image to the repository.
 *
 * @param name
 *            The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
 */
public class PushImageCmdImpl extends AbstrAsyncDockerCmd<PushImageCmd, PushResponseItem> implements PushImageCmd {

    private String name;

    private String tag;

    private AuthConfig authConfig;

    public PushImageCmdImpl(PushImageCmd.Exec exec, AuthConfig authConfig, String name) {
        super(exec);
        withName(name);
        withAuthConfig(authConfig);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTag() {
        return tag;
    }

    /**
     * @param name
     *            The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
     */
    @Override
    public PushImageCmd withName(String name) {
        this.name = Objects.requireNonNull(name, "name was not specified");
        return this;
    }

    /**
     * @param tag
     *            The image's tag. Can be null or empty.
     */
    @Override
    public PushImageCmd withTag(String tag) {
        this.tag = Objects.requireNonNull(tag, "tag was not specified");
        return this;
    }

    public AuthConfig getAuthConfig() {
        return authConfig;
    }

    public PushImageCmd withAuthConfig(AuthConfig authConfig) {
        this.authConfig = authConfig;
        return this;
    }
}
