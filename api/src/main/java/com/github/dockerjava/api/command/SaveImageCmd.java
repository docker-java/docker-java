package com.github.dockerjava.api.command;

import java.io.InputStream;

import com.github.dockerjava.api.NotFoundException;

public interface SaveImageCmd extends SyncDockerCmd<InputStream> {

    public String getName();

    public String getTag();

    /**
     * @param name
     *            The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
     */
    public SaveImageCmd withName(String name);

    /**
     * @param tag
     *            The image's tag. Not null.
     */
    public SaveImageCmd withTag(String tag);

    /**
     * Its the responsibility of the caller to consume and/or close the {@link InputStream} to prevent connection leaks.
     *
     * @throws com.github.dockerjava.api.NotFoundException
     *             No such image
     */
    public InputStream exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<SaveImageCmd, InputStream> {
    }

}
