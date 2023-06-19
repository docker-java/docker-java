package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PullResponseItem;

/**
 *
 * Pull image from repository.
 *
 */
public class PullImageCmdImpl extends AbstrAsyncDockerCmd<PullImageCmd, PullResponseItem> implements PullImageCmd {

    private String repository, tag, platform, registry;

    private AuthConfig authConfig;

    public PullImageCmdImpl(PullImageCmd.Exec exec, AuthConfig authConfig, String repository) {
        super(exec);
        withAuthConfig(authConfig);
        withRepository(repository);
    }

    public AuthConfig getAuthConfig() {
        return authConfig;
    }

    public PullImageCmd withAuthConfig(AuthConfig authConfig) {
        this.authConfig = authConfig;
        return this;
    }

    @Override
    public String getRepository() {
        return repository;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public String getPlatform() {
        return platform;
    }

    @Override
    public String getRegistry() {
        return registry;
    }

    @Override
    public PullImageCmd withRepository(String repository) {
        this.repository = Objects.requireNonNull(repository, "repository was not specified");
        return this;
    }

    @Override
    public PullImageCmd withTag(String tag) {
        this.tag = Objects.requireNonNull(tag, "tag was not specified");
        return this;
    }

    @Override
    public PullImageCmd withPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    @Override
    public PullImageCmd withRegistry(String registry) {
        this.registry = Objects.requireNonNull(registry, "registry was not specified");
        return this;
    }

}
