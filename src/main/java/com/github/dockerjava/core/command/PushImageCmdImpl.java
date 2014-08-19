package com.github.dockerjava.core.command;

import java.io.InputStream;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.command.PushImageCmd;
import com.google.common.base.Preconditions;

/**
 * Push the latest image to the repository.
 *
 * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
 */
public class PushImageCmdImpl extends AbstrAuthCfgDockerCmd<PushImageCmd, InputStream> implements PushImageCmd  {

	private String name;

	public PushImageCmdImpl(DockerCmdExec<PushImageCmd, InputStream> exec, String name) {
		super(exec);
		withName(name);
	}

    @Override
	public String getName() {
        return name;
    }

    /**
	 * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
	 */
	@Override
	public PushImageCmd withName(String name) {
		Preconditions.checkNotNull(name, "name was not specified");
		this.name = name;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("push ")
            .append(name)
            .toString();
    }
   
    /**
     * @throws NotFoundException No such image
     */
	@Override
    public InputStream exec() throws NotFoundException {
    	return super.exec();
    }

//	protected InputStream impl() {
//		WebTarget webResource = baseResource.path("/images/" + name(name) + "/push");
//
//		final String registryAuth = registryAuth();
//		LOGGER.trace("POST: {}", webResource);
//		return webResource
//                .request()
//				.header("X-Registry-Auth", registryAuth)
//				.accept(MediaType.APPLICATION_JSON)
//				.post(entity(Response.class, MediaType.APPLICATION_JSON)).readEntity(InputStream.class);
//	}
//
//	private String name(String name) {
//		return name.contains("/") ? name : authConfig.getUsername();
//	}
}
