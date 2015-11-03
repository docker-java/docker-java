package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Image;

import java.util.List;

/**
 * List images
 *
 * @param showAll
 *            - Show all images (by default filter out the intermediate images used to build)
 * @param filters
 *            - a json encoded value of the filters (a map[string][]string) to process on the images list.
 */
public interface ListImagesCmd extends SyncDockerCmd<List<Image>> {

    public String getFilters();

    public boolean hasShowAllEnabled();

    public ListImagesCmd withShowAll(boolean showAll);

    public ListImagesCmd withFilters(String filters);

    public static interface Exec extends DockerCmdSyncExec<ListImagesCmd, List<Image>> {
    }

}