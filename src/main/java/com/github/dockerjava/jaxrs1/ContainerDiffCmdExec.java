package com.github.dockerjava.jaxrs1;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.ContainerDiffCmd;
import com.github.dockerjava.api.model.ChangeLog;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

public class ContainerDiffCmdExec extends AbstrDockerCmdExec<ContainerDiffCmd, List<ChangeLog>> implements ContainerDiffCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContainerDiffCmdExec.class);
	
	public ContainerDiffCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected List<ChangeLog> execute(ContainerDiffCmd command) {
		WebResource webResource = getBaseResource()
		        .resolveTemplate("/containers/{id}/changes", command.getContainerId())
		        .build();
		
		LOGGER.trace("GET: {}", webResource);
		return webResource
		        .accept(MediaType.APPLICATION_JSON)
		        .get(new GenericType<List<ChangeLog>>() {
        });
	}

}
