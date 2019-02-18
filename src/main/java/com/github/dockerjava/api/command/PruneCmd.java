package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.PruneResponse;
import com.github.dockerjava.api.model.PruneType;
import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * Delete unused content (containers, images, volumes, networks, build relicts)
 *
 * @since {@link RemoteApiVersion#VERSION_1_25}
 */
public interface PruneCmd extends SyncDockerCmd<PruneResponse> {

    @Nonnull
    PruneType getPruneType();

    @Nonnull
    String getApiPath();

    @CheckForNull
    Map<String, List<String>> getFilters();

    PruneCmd withPruneType(final PruneType pruneType);
    /**
     * Prune containers created before this timestamp
     * Meaningful only for CONTAINERS and IMAGES prune type
     * @param until Can be Unix timestamps, date formatted timestamps, or Go duration strings (e.g. 10m, 1h30m) computed relative to the daemon machine’s time.
     */
    PruneCmd withUntilFilter(String until);

    /**
     * When set to true, prune only unused and untagged images. When set to false, all unused images are pruned.
     * Meaningful only for IMAGES prune type
     */
    PruneCmd withDangling(Boolean dangling);

    /**
     * Prune containers with the specified labels
     */
    PruneCmd withLabelFilter(String... label);

    @Override
    PruneResponse exec();

    interface Exec extends DockerCmdSyncExec<PruneCmd, PruneResponse> {
    }

}
