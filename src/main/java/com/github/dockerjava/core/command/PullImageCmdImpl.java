package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.google.common.base.Preconditions;

import java.io.InputStream;

/**
 *
 * Pull image from repository.
 *
 */
public class PullImageCmdImpl extends AbstrDockerCmd<PullImageCmd, InputStream> implements PullImageCmd {

    private String repository, tag, registry;
    private AuthConfig authConfig;

	public PullImageCmdImpl(PullImageCmd.Exec exec, String repository) {
		super(exec);
		withRepository(repository);
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

    public AuthConfig getAuthConfig() {
        return authConfig;
    }

    @Override
	public PullImageCmd withRepository(String repository) {
		Preconditions.checkNotNull(repository, "repository was not specified");
		this.repository = repository;
		return this;
	}

	@Override
	public PullImageCmd withTag(String tag) {
		Preconditions.checkNotNull(tag, "tag was not specified");
		this.tag = tag;
		return this;
	}

	@Override
	public PullImageCmd withRegistry(String registry) {
		Preconditions.checkNotNull(registry, "registry was not specified");
		this.registry = registry;
		return this;
	}

    @Override
    public PullImageCmd withAuthConfig(AuthConfig authConfig) {
		Preconditions.checkNotNull(authConfig, "authConfig was not specified");
        this.authConfig = authConfig;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder("pull ")
            .append(repository)
            .append(tag != null ? ":" + tag : "")
            .toString();
    }
}
