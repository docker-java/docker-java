package com.github.dockerjava.jaxrs1;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

public class ListContainersCmdExec extends AbstrDockerCmdExec<ListContainersCmd, List<Container>> implements ListContainersCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ListContainersCmdExec.class);
	
	public ListContainersCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected List<Container> execute(ListContainersCmd command) {
		WebResource webResource = getBaseResource().path("/containers/json")
                .queryParam("all", command.hasShowAllEnabled() ? "1" : "0")
                .queryParam("since", command.getSinceId())
                .queryParam("before", command.getBeforeId())
                .queryParam("size", command.hasShowSizeEnabled() ? "1" : "0")
                .build();

        if (command.getLimit() >= 0) {
            webResource = webResource.queryParam("limit", String.valueOf(command.getLimit()));
        }

		LOGGER.trace("GET: {}", webResource);
		List<Container> containers = webResource
		        .accept(MediaType.APPLICATION_JSON)
		        .get(new GenericType<List<Container>>() {});
		LOGGER.trace("Response: {}", containers);

		return containers;
	}

}
