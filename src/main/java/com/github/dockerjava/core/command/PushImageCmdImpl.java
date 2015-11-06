package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

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

    public PushImageCmdImpl(PushImageCmd.Exec exec, String name) {
        super(exec);
        withName(name);
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
        checkNotNull(name, "name was not specified");
        this.name = name;
        return this;
    }

    /**
     * @param tag
     *            The image's tag. Can be null or empty.
     */
    @Override
    public PushImageCmd withTag(String tag) {
        checkNotNull(tag, "tag was not specified");
        this.tag = tag;
        return this;
    }

    public AuthConfig getAuthConfig() {
        return authConfig;
    }

    public PushImageCmd withAuthConfig(AuthConfig authConfig) {
        checkNotNull(authConfig, "authConfig was not specified");
        return withOptionalAuthConfig(authConfig);
    }

    private PushImageCmd withOptionalAuthConfig(AuthConfig authConfig) {
        this.authConfig = authConfig;
        return this;
    }

}
