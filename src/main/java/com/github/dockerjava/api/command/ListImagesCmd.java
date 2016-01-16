package com.github.dockerjava.api.command;

import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;

import com.github.dockerjava.api.model.Image;

/**
 * List images
 */
public interface ListImagesCmd extends SyncDockerCmd<List<Image>> {

    @CheckForNull
    public Map<String, List<String>> getFilters();

    public String getImageNameFilter();

    @CheckForNull
    public Boolean hasShowAllEnabled();

    /**
     * Show all images (by default filter out the intermediate images used to build)
     */
    public ListImagesCmd withShowAll(Boolean showAll);

    public ListImagesCmd withImageNameFilter(String imageName);

    /**
     * Filter dangling images
     */
    public ListImagesCmd withDanglingFilter(Boolean dangling);

    /**
     * @param labels
     *            - string array in the form ["key"] or ["key=value"] or a mix of both
     */
    public ListImagesCmd withLabelsFilter(String... labels);

    /**
     * @param labels
     *            - {@link Map} of labels that contains label keys and values
     */
    public ListImagesCmd withLabelsFilter(Map<String, String> labels);

    public static interface Exec extends DockerCmdSyncExec<ListImagesCmd, List<Image>> {
    }

}
