package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PushResponseItem;

/**
 * Push the latest image to the repository.
 *
 * @param name
 *            The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
 */
public interface PushImageCmd extends AsyncDockerCmd<PushImageCmd, PushResponseItem> {

    @CheckForNull
    public AuthConfig getAuthConfig();

    @CheckForNull
    public String getName();

    @CheckForNull
    public String getTag();

    /**
     * @param name
     *            The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
     */
    public PushImageCmd withName(@Nonnull String name);

    /**
     * @param tag
     *            The image's tag. Not null.
     */
    public PushImageCmd withTag(String tag);

    public PushImageCmd withAuthConfig(AuthConfig authConfig);

    /**
     * @throws NotFoundException
     *             No such image
     */
    @Override
    public <T extends ResultCallback<PushResponseItem>> T exec(T resultCallback);

    public static interface Exec extends DockerCmdAsyncExec<PushImageCmd, PushResponseItem> {
    }
}