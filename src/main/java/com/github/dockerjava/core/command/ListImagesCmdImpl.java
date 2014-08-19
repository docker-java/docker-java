package com.github.dockerjava.core.command;

import java.util.List;

import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Image;
import com.google.common.base.Preconditions;

/**
 * List images
 *
 * @param showAll - Show all images (by default filter out the intermediate images used to build)
 * @param filter - TODO: undocumented in docker remote api reference
 */
public class ListImagesCmdImpl extends AbstrDockerCmd<ListImagesCmd, List<Image>> implements ListImagesCmd  {

	private String filter;
	
	private boolean showAll = false;
	
	public ListImagesCmdImpl(DockerCmdExec<ListImagesCmd, List<Image>> exec) {
		super(exec);
	}

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
    
//	protected List<Image> impl() {
//
//		WebTarget webResource = baseResource
//                .path("/images/json")
//                .queryParam("filter", filter)
//                .queryParam("all", showAll ? "1" : "0");
//
//		LOGGER.trace("GET: {}", webResource);
//		List<Image> images = webResource.request().accept(MediaType.APPLICATION_JSON).get(new GenericType<List<Image>>() {
//		});
//		LOGGER.trace("Response: {}", images);
//		return images;
//	}
}
