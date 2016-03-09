package com.github.dockerjava.api.command;

import javax.annotation.Nonnull;

/**
 * Inspect the details of a volume.
 *
 * @author Marcus Linke
 *
 */
public interface InspectVolumeCmd extends SyncDockerCmd<InspectVolumeResponse> {

    String getName();

    /**
     * @param name
     *            - The volume’s name.
     */
    InspectVolumeCmd withName(@Nonnull String name);

    interface Exec extends DockerCmdSyncExec<InspectVolumeCmd, InspectVolumeResponse> {
    }
}
