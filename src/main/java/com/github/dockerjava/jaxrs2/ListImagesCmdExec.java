package com.github.dockerjava.jaxrs2;

import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Image;

public class ListImagesCmdExec extends AbstrDockerCmdExec<ListImagesCmd, List<Image>> implements ListImagesCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ListImagesCmdExec.class);
	
	public ListImagesCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected List<Image> execute(ListImagesCmd command) {
		WebTarget webResource = getBaseResource()
                .path("/images/json")
                .queryParam("filter", command.getFilter())
                .queryParam("all", command.hasShowAllEnabled() ? "1" : "0");

		LOGGER.trace("GET: {}", webResource);
		
		List<Image> images = webResource.request().accept(MediaType.APPLICATION_JSON).get(new GenericType<List<Image>>() {
		});
		LOGGER.trace("Response: {}", images);
		
		return images;
	}

}
