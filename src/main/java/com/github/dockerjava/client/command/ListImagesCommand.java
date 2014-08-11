package com.github.dockerjava.client.command;

import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Image;
import com.google.common.base.Preconditions;

/**
 * List images
 *
 * @param showAll - Show all images (by default filter out the intermediate images used to build)
 * @param filter - TODO: undocumented in docker remote api reference
 */
public class ListImagesCommand extends AbstrDockerCmd<ListImagesCommand, List<Image>> implements ListImagesCmd  {

	private static final Logger LOGGER = LoggerFactory.getLogger(ListImagesCommand.class);

	private String filter;
	private boolean showAll = false;

    @Override
	public String getFilter() {
        return filter;
    }

    @Override
	public boolean hasShowAllEnabled() {
        return showAll;
    }

    @Override
	public ListImagesCmd withShowAll(boolean showAll) {
		this.showAll = showAll;
		return this;
	}

	@Override
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

		LOGGER.trace("GET: {}", webResource);
		List<Image> images = webResource.request().accept(MediaType.APPLICATION_JSON).get(new GenericType<List<Image>>() {
		});
		LOGGER.trace("Response: {}", images);
		return images;
	}
}
