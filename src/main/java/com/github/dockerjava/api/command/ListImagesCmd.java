package com.github.dockerjava.api.command;

import java.util.List;

import javax.annotation.CheckForNull;

import com.github.dockerjava.api.model.Image;

/**
 * List images
 *
 * @param showAll
 *            - Show all images (by default filter out the intermediate images used to build)
 * @param filters
 *            - a json encoded value of the filters (a map[string][]string) to process on the images list.
 */
public interface ListImagesCmd extends SyncDockerCmd<List<Image>> {

    @CheckForNull
    public String getFilters();
    
    public String getImageNameFilter();

    @CheckForNull
    public Boolean hasShowAllEnabled();

    public ListImagesCmd withShowAll(Boolean showAll);

    public ListImagesCmd withImageNameFilter(String imageName);
    
    public ListImagesCmd withFilters(String filters);

    public static interface Exec extends DockerCmdSyncExec<ListImagesCmd, List<Image>> {
    }

}