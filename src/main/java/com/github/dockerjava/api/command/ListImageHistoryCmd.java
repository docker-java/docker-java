package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.History;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;

public interface ListImageHistoryCmd extends SyncDockerCmd<List<History>> {

    @CheckForNull
    String getImageId();

    ListImageHistoryCmd withImageId(@Nonnull String imageId);

    interface Exec extends DockerCmdSyncExec<ListImageHistoryCmd, List<History>> {
    }

}
