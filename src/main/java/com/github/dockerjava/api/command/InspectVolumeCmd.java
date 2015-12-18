package com.github.dockerjava.api.command;

import javax.annotation.Nonnull;

/**
 * Inspect the details of a volume.
 *
 * @author Marcus Linke
 *
 */
public interface InspectVolumeCmd extends SyncDockerCmd<InspectVolumeResponse> {

    public String getName();

    /**
     * @param name
     *            - The volume’s name.
     */
    public InspectVolumeCmd withName(@Nonnull String name);

    public static interface Exec extends DockerCmdSyncExec<InspectVolumeCmd, InspectVolumeResponse> {
    }
}
