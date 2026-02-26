package com.github.dockerjava.api.command;

import java.io.InputStream;
import java.util.List;

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
     * Confines the saved image to the specified platform or platforms (if invoked repeatedly).
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_48} for one platform and
     * {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_52} for multiple platforms.
     * @return this
     */
    SaveImageCmd withPlatform(String platform);

    /**
     * Gets the platforms that were added by {@link #withPlatform(String)}.
     * @return platforms the saved image should be confined to.
     */
    List<String> getPlatforms();

    /**
     * It's the responsibility of the caller to consume and/or close the {@link InputStream} to prevent connection leaks.
     *
     * @throws NotFoundException
     *             No such image
     */
    InputStream exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<SaveImageCmd, InputStream> {
    }

}
