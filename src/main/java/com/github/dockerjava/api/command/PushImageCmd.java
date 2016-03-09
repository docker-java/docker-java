package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.exception.NotFoundException;
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
    AuthConfig getAuthConfig();

    @CheckForNull
    String getName();

    @CheckForNull
    String getTag();

    /**
     * @param name
     *            The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
     */
    PushImageCmd withName(@Nonnull String name);

    /**
     * @param tag
     *            The image's tag. Not null.
     */
    PushImageCmd withTag(String tag);

    PushImageCmd withAuthConfig(AuthConfig authConfig);

    /**
     * @throws NotFoundException
     *             No such image
     */
    @Override
    <T extends ResultCallback<PushResponseItem>> T exec(T resultCallback);

    interface Exec extends DockerCmdAsyncExec<PushImageCmd, PushResponseItem> {
    }
}
