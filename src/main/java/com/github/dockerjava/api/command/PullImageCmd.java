package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PullResponseItem;

/**
 *
 * Pull image from repository.
 *
 */
public interface PullImageCmd extends AsyncDockerCmd<PullImageCmd, PullResponseItem> {

    public String getRepository();

    public String getTag();

    public String getRegistry();

    public AuthConfig getAuthConfig();

    public PullImageCmd withRepository(String repository);

    public PullImageCmd withTag(String tag);

    public PullImageCmd withRegistry(String registry);

    public PullImageCmd withAuthConfig(AuthConfig authConfig);

    public static interface Exec extends DockerCmdAsyncExec<PullImageCmd, PullResponseItem> {
    }
}