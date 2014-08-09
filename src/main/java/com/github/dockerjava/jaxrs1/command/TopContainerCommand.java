package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.TopContainerResponse;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;

/**
 * List processes running inside a container
 */
public class TopContainerCommand extends AbstrDockerCmd<TopContainerCommand, TopContainerResponse> implements TopContainerCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopContainerCommand.class);

	private String containerId;

	private String psArgs;

	public TopContainerCommand(String containerId) {
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public String getPsArgs() {
        return psArgs;
    }

    @Override
	public TopContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}


	@Override
	public TopContainerCmd withPsArgs(String psArgs) {
		Preconditions.checkNotNull(psArgs, "psArgs was not specified");
		this.psArgs = psArgs;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("top ")
            .append(containerId)
            .append(psArgs != null ? " " + psArgs : "")
            .toString();
    }

    /**
     * @throws NotFoundException No such container
     */
	@Override
    public TopContainerResponse exec() throws NotFoundException {
    	return super.exec();
    }
    
	protected TopContainerResponse impl() throws DockerException {
		WebResource webResource = baseResource.path(String.format("/containers/%s/top", containerId));

		if(!StringUtils.isEmpty(psArgs))
			webResource = webResource.queryParam("ps_args", psArgs);
		
		LOGGER.trace("GET: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON).get(TopContainerResponse.class);
	}
}
