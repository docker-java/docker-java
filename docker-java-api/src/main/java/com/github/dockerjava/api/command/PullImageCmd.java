package com.github.dockerjava.api.command;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PullResponseItem;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 *
 * Pull image from repository.
 *
 */
public interface PullImageCmd extends AsyncDockerCmd<PullImageCmd, PullResponseItem> {

    @CheckForNull
    String getRepository();

    @CheckForNull
    String getTag();

    @CheckForNull
    String getPlatform();

    @CheckForNull
    String getRegistry();

    @CheckForNull
    AuthConfig getAuthConfig();

    PullImageCmd withRepository(@Nonnull String repository);

    PullImageCmd withTag(String tag);

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_32}
     */
    PullImageCmd withPlatform(String tag);

    PullImageCmd withRegistry(String registry);

    PullImageCmd withAuthConfig(AuthConfig authConfig);

    @Override
    default ResultCallback.Adapter<PullResponseItem> start() {
        return exec(new PullImageResultCallback());
    }

    interface Exec extends DockerCmdAsyncExec<PullImageCmd, PullResponseItem> {
    }
}
