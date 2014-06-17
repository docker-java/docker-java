package com.github.dockerjava.client.command;

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.Image;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


/**
 * List images
 * 
 * @param showAll - Show all images (by default filter out the intermediate images used to build)
 * @param filter - TODO: undocumented in docker remote api reference
 */
public class ListImagesCmd extends AbstrDockerCmd<ListImagesCmd, List<Image>>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(ListImagesCmd.class);
	
	private String filter;
	private boolean showAll = false;
	
	public ListImagesCmd withShowAll(boolean showAll) {
		this.showAll = showAll;
		return this;
	}
	
	public ListImagesCmd withFilter(String filter) {
		Preconditions.checkNotNull(filter, "filter was not specified");
		this.filter = filter;
		return this;
	}
	
	protected List<Image> impl() {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("filter", filter);
		params.add("all", showAll ? "1" : "0");

		WebResource webResource = baseResource.path("/images/json").queryParams(params);

		try {
			LOGGER.trace("GET: {}", webResource);
			List<Image> images = webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<Image>>() {
			});
			LOGGER.trace("Response: {}", images);
			return images;
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 400) {
				throw new DockerException("bad parameter");
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException();
			}
		}
	}
}
