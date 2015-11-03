package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.SearchItem;

import java.util.List;

/**
 * Search images
 *
 * @param term
 *            - search term
 *
 */
public interface SearchImagesCmd extends SyncDockerCmd<List<SearchItem>> {

    public String getTerm();

    public SearchImagesCmd withTerm(String term);

    public static interface Exec extends DockerCmdSyncExec<SearchImagesCmd, List<SearchItem>> {
    }

}