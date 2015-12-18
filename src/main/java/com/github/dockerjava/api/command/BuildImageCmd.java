package com.github.dockerjava.api.command;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.BuildResponseItem;

/**
 * Build an image from Dockerfile.
 * <p>
 * TODO: http://docs.docker.com/reference/builder/#dockerignore
 *
 * @see <a
 *      href="https://docs.docker.com/reference/api/docker_remote_api_v1.20/#build-image-from-a-dockerfile">build-image-from-a-dockerfile</a>
 */
public interface BuildImageCmd extends AsyncDockerCmd<BuildImageCmd, BuildResponseItem> {

    // lib specific

    @CheckForNull
    public InputStream getTarInputStream();

    @CheckForNull
    public AuthConfigurations getBuildAuthConfigs();

    // getters

    /**
     * "t" in API
     */
    @CheckForNull
    public String getTag();

    /**
     * "remote" in API
     */
    @CheckForNull
    public URI getRemote();

    /**
     * "nocache" in API
     */
    @CheckForNull
    public Boolean hasNoCacheEnabled();

    /**
     * "rm" in API
     */
    @CheckForNull
    public Boolean hasRemoveEnabled();

    /**
     * "forcerm" in API
     */
    @CheckForNull
    public Boolean isForcerm();

    /**
     * "q" in API
     */
    @CheckForNull
    public Boolean isQuiet();

    /**
     * "pull" in API
     */
    @CheckForNull
    public Boolean hasPullEnabled();

    @CheckForNull
    public String getPathToDockerfile();

    @CheckForNull
    public Long getMemory();

    @CheckForNull
    public Long getMemswap();

    @CheckForNull
    public String getCpushares();

    @CheckForNull
    public String getCpusetcpus();

    // setters

    public BuildImageCmd withTag(String tag);

    public BuildImageCmd withRemote(URI remote);

    public BuildImageCmd withBaseDirectory(File baseDirectory);

    public BuildImageCmd withDockerfile(File dockerfile);

    public BuildImageCmd withNoCache(Boolean noCache);

    public BuildImageCmd withRemove(Boolean rm);

    public BuildImageCmd withForcerm(Boolean forcerm);

    public BuildImageCmd withQuiet(Boolean quiet);

    public BuildImageCmd withPull(Boolean pull);

    public BuildImageCmd withMemory(Long memory);

    public BuildImageCmd withMemswap(Long memswap);

    public BuildImageCmd withCpushares(String cpushares);

    public BuildImageCmd withCpusetcpus(String cpusetcpus);

    // setters lib specific

    public BuildImageCmd withBuildAuthConfigs(AuthConfigurations authConfig);

    public BuildImageCmd withTarInputStream(@Nonnull InputStream tarInputStream);

    public static interface Exec extends DockerCmdAsyncExec<BuildImageCmd, BuildResponseItem> {
    }

}
