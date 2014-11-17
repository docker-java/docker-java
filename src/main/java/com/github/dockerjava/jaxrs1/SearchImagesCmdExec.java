package com.github.dockerjava.jaxrs1;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.model.SearchItem;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

public class SearchImagesCmdExec extends AbstrDockerCmdExec<SearchImagesCmd, List<SearchItem>> implements SearchImagesCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchImagesCmdExec.class);
	
	public SearchImagesCmdExec(WebResource baseResource) {
		super(baseResource);
	}

	@Override
	protected List<SearchItem> execute(SearchImagesCmd command) {
		WebResource webResource = getBaseResource()
		        .path("/images/search")
		        .queryParam("term", command.getTerm())
		        .build();
		
		LOGGER.trace("GET: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON)
		        .get(new GenericType<List<SearchItem>>() {});
	}

}
