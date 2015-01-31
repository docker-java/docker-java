package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.AuthConfig;

/**
 *
 * Pull image from repository.
 *
 */
public class PullImageCmdImpl extends AbstrAuthCfgDockerCmd<PullImageCmd, InputStream> implements PullImageCmd {

    private String repository, tag, registry;

	public PullImageCmdImpl(PullImageCmd.Exec exec, AuthConfig authConfig, String repository) {
		super(exec, authConfig);
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

    @Override
    public String toString() {
        return new StringBuilder("pull ")
            .append(repository)
            .append(tag != null ? ":" + tag : "")
            .toString();
    }
}
