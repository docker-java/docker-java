package com.github.dockerjava.client.command;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.SearchItem;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;


/**
 * 
 * 
 *
 */
public class SearchImagesCmd extends AbstrDockerCmd<SearchImagesCmd, List<SearchItem>>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchImagesCmd.class);
	
	private String term;
	
	public SearchImagesCmd(String term) {
		withTerm(term);
	}
	
	public SearchImagesCmd withTerm(String term) {
		Preconditions.checkNotNull(term, "term was not specified");
		this.term = term;
		return this;
	}
	
    @Override
    public String toString() {
        return new StringBuilder("search ")
            .append(term)
            .toString();
    }   

	protected List<SearchItem> impl() {
		WebResource webResource = baseResource.path("/images/search").queryParam("term", term);
		try {
			LOGGER.trace("GET: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_JSON).get(new GenericType<List<SearchItem>>() {
			});
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error.", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}
}
