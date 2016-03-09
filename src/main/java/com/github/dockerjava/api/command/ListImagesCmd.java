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
    Map<String, List<String>> getFilters();

    String getImageNameFilter();

    @CheckForNull
    Boolean hasShowAllEnabled();

    /**
     * Show all images (by default filter out the intermediate images used to build)
     */
    ListImagesCmd withShowAll(Boolean showAll);

    ListImagesCmd withImageNameFilter(String imageName);

    /**
     * Filter dangling images
     */
    ListImagesCmd withDanglingFilter(Boolean dangling);

    /**
     * @param labels
     *            - string array in the form ["key"] or ["key=value"] or a mix of both
     */
    ListImagesCmd withLabelFilter(String... label);

    /**
     * @param labels
     *            - {@link Map} of labels that contains label keys and values
     */
    ListImagesCmd withLabelFilter(Map<String, String> labels);

    interface Exec extends DockerCmdSyncExec<ListImagesCmd, List<Image>> {
    }

}
