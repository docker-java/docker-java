package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PullResponseItem;

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
    String getRegistry();

    AuthConfig getAuthConfig();

    PullImageCmd withRepository(@Nonnull String repository);

    PullImageCmd withTag(String tag);

    PullImageCmd withRegistry(String registry);

    PullImageCmd withAuthConfig(AuthConfig authConfig);

    interface Exec extends DockerCmdAsyncExec<PullImageCmd, PullResponseItem> {
    }
}
