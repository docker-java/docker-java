package com.github.dockerjava.api.command;

import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

public interface SaveImageCmd extends SyncDockerCmd<InputStream> {

    @CheckForNull
    String getName();

    @CheckForNull
    String getTag();

    /**
     * @param name
     *            The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
     */
    SaveImageCmd withName(@Nonnull String name);

    /**
     * @param tag
     *            The image's tag. Not null.
     */
    SaveImageCmd withTag(String tag);

    /**
     * Its the responsibility of the caller to consume and/or close the {@link InputStream} to prevent connection leaks.
     *
     * @throws NotFoundException
     *             No such image
     */
    InputStream exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<SaveImageCmd, InputStream> {
    }

}
