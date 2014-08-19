package com.github.dockerjava.core.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.TopContainerResponse;
import com.google.common.base.Preconditions;

/**
 * List processes running inside a container
 */
public class TopContainerCmdImpl extends AbstrDockerCmd<TopContainerCmd, TopContainerResponse> implements TopContainerCmd {

	private String containerId;

	private String psArgs;

	public TopContainerCmdImpl(DockerCmdExec<TopContainerCmd, TopContainerResponse> exec, String containerId) {
		super(exec);
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
    
//	protected TopContainerResponse impl() throws DockerException {
//		WebTarget webResource = baseResource.path("/containers/{id}/top")
//				.resolveTemplate("id", containerId);
//
//		if(!StringUtils.isEmpty(psArgs))
//			webResource = webResource.queryParam("ps_args", psArgs);
//		
//		LOGGER.trace("GET: {}", webResource);
//		return webResource.request().accept(MediaType.APPLICATION_JSON).get(TopContainerResponse.class);
//	}		
}
