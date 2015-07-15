package com.github.dockerjava.api.command;

import java.io.File;
import java.io.InputStream;

import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.BuildResponseItem;

/**
 *
 * Build an image from Dockerfile.
 *
 * TODO: http://docs.docker.com/reference/builder/#dockerignore
 *
 */
public interface BuildImageCmd extends AsyncDockerCmd<BuildImageCmd, BuildResponseItem> {

    public BuildImageCmd withTag(String tag);

    public InputStream getTarInputStream();

    public String getTag();

    public boolean hasNoCacheEnabled();

    public boolean hasRemoveEnabled();

    public boolean isQuiet();

    public boolean hasPullEnabled();

    public String getPathToDockerfile();

    public AuthConfigurations getBuildAuthConfigs();

    public BuildImageCmd withBaseDirectory(File baseDirectory);

    public BuildImageCmd withDockerfile(File dockerfile);

    public BuildImageCmd withTarInputStream(InputStream tarInputStream);

    public BuildImageCmd withNoCache();

    public BuildImageCmd withNoCache(boolean noCache);

    public BuildImageCmd withRemove();

    public BuildImageCmd withRemove(boolean rm);

    public BuildImageCmd withQuiet();

    public BuildImageCmd withQuiet(boolean quiet);

    public BuildImageCmd withPull();

    public BuildImageCmd withPull(boolean pull);

    public BuildImageCmd withBuildAuthConfigs(AuthConfigurations authConfig);

    public static interface Exec extends DockerCmdAsyncExec<BuildImageCmd, BuildResponseItem> {
    }



}