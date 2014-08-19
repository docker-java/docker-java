package com.github.dockerjava.core.command;

import java.io.InputStream;

import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.command.PullImageCmd;
import com.google.common.base.Preconditions;

/**
 *
 * Pull image from repository.
 *
 */
public class PullImageCmdImpl extends AbstrDockerCmd<PullImageCmd, InputStream> implements PullImageCmd {

	private String repository, tag, registry;

	public PullImageCmdImpl(DockerCmdExec<PullImageCmd, InputStream> exec, String repository) {
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
    public String toString() {
        return new StringBuilder("pull ")
            .append(repository)
            .append(tag != null ? ":" + tag : "")
            .toString();
    }

//	protected InputStream impl() {
//
//		WebTarget webResource = baseResource.path("/images/create")
//                .queryParam("tag", tag)
//                .queryParam("fromImage", repository)
//                .queryParam("registry", registry);
//		
//		LOGGER.trace("POST: {}", webResource);
//		return webResource.request()
//				.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
//				.post(entity(Response.class, MediaType.APPLICATION_JSON)).readEntity(InputStream.class);
//	}
}
