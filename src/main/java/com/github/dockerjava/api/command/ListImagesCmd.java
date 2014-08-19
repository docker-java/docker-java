package com.github.dockerjava.api.command;

import java.util.List;

import com.github.dockerjava.api.model.Image;

/**
 * List images
 *
 * @param showAll - Show all images (by default filter out the intermediate images used to build)
 * @param filter - TODO: undocumented in docker remote api reference
 */
public interface ListImagesCmd extends DockerCmd<List<Image>> {

	public String getFilter();

	public boolean hasShowAllEnabled();

	public ListImagesCmd withShowAll(boolean showAll);

	public ListImagesCmd withFilter(String filter);
	
	public static interface Exec extends DockerCmdExec<ListImagesCmd, List<Image>> {
	}

}