package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.google.common.base.Preconditions;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author marcus
 *
 */
public class TopContainerCmd extends AbstrDockerCmd<TopContainerCmd, TopContainerResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopContainerCmd.class);

	private String containerId;

	private String psArgs;

	public TopContainerCmd(String containerId) {
		withContainerId(containerId);
	}

    public String getContainerId() {
        return containerId;
    }

    public String getPsArgs() {
        return psArgs;
    }

    public TopContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}


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

	protected TopContainerResponse impl() throws DockerException {
		WebTarget webResource = baseResource.path("/containers/{id}/top").resolveTemplate("id", containerId);

		if(!StringUtils.isEmpty(psArgs))
			webResource = webResource.queryParam("ps_args", psArgs);

		try {
			LOGGER.trace("GET: {}", webResource);
			return webResource.request().accept(MediaType.APPLICATION_JSON).get(TopContainerResponse.class);
		} catch (ClientErrorException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}
}
