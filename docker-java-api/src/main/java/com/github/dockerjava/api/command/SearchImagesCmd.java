package com.github.dockerjava.api.command;

import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.model.SearchItem;

/**
 * Search images
 *
 * @param term
 *            - search term
 *
 */
public interface SearchImagesCmd extends SyncDockerCmd<List<SearchItem>> {

    @CheckForNull
    String getTerm();
    Integer getLimit();

    SearchImagesCmd withTerm(@Nonnull String term);
    SearchImagesCmd withLimit(@Nonnull Integer limit);

    interface Exec extends DockerCmdSyncExec<SearchImagesCmd, List<SearchItem>> {
    }

}
