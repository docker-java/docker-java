package com.github.dockerjava.api.command;

import java.util.List;

import com.github.dockerjava.api.model.Image;

/**
 * List images
 *
 * @param showAll - Show all images (by default filter out the intermediate images used to build)
 * @param filters - a json encoded value of the filters (a map[string][]string) to process on the images list.
 */
public interface ListImagesCmd extends DockerCmd<List<Image>> {

	public String getFilters();

	public boolean hasShowAllEnabled();

	public ListImagesCmd withShowAll(boolean showAll);

	public ListImagesCmd withFilters(String filters);

	public static interface Exec extends DockerCmdExec<ListImagesCmd, List<Image>> {
	}

}