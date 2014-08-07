package com.github.dockerjava.client.command;

import java.util.List;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.Image;
import com.google.common.base.Preconditions;
import javax.ws.rs.client.WebTarget;


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

    public String getFilter() {
        return filter;
    }

    public boolean hasShowAllEnabled() {
        return showAll;
    }

    public ListImagesCmd withShowAll(boolean showAll) {
		this.showAll = showAll;
		return this;
	}

	public ListImagesCmd withFilter(String filter) {
		Preconditions.checkNotNull(filter, "filter was not specified");
		this.filter = filter;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("images ")
            .append(showAll ? "--all=true" : "")
            .append(filter != null ? "--filter " + filter : "")
            .toString();
    }

	protected List<Image> impl() {
		WebTarget webResource = baseResource
                .path("/images/json")
                .queryParam("filter", filter)
                .queryParam("all", showAll ? "1" : "0");

		try {
			LOGGER.trace("GET: {}", webResource);
			List<Image> images = webResource.request().accept(MediaType.APPLICATION_JSON).get(new GenericType<List<Image>>() {
			});
			LOGGER.trace("Response: {}", images);
			return images;
		} catch (ClientErrorException exception) {
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
