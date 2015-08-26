package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PullResponseItem;

/**
 *
 * Pull image from repository.
 *
 */
public class PullImageCmdImpl extends AbstrAsyncDockerCmd<PullImageCmd, PullResponseItem> implements PullImageCmd {

    private String repository, tag, registry;

    private AuthConfig authConfig;

    public PullImageCmdImpl(PullImageCmd.Exec exec, AuthConfig authConfig, String repository) {
        super(exec);
        withOptionalAuthConfig(authConfig);
        withRepository(repository);
    }

    public AuthConfig getAuthConfig() {
        return authConfig;
    }

    public PullImageCmd withAuthConfig(AuthConfig authConfig) {
        checkNotNull(authConfig, "authConfig was not specified");
        return withOptionalAuthConfig(authConfig);
    }

    private PullImageCmd withOptionalAuthConfig(AuthConfig authConfig) {
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
    public String getRegistry() {
        return registry;
    }

    @Override
    public PullImageCmd withRepository(String repository) {
        checkNotNull(repository, "repository was not specified");
        this.repository = repository;
        return this;
    }

    @Override
    public PullImageCmd withTag(String tag) {
        checkNotNull(tag, "tag was not specified");
        this.tag = tag;
        return this;
    }

    @Override
    public PullImageCmd withRegistry(String registry) {
        checkNotNull(registry, "registry was not specified");
        this.registry = registry;
        return this;
    }

}
