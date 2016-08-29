package com.github.dockerjava.api.command;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.RemoteApiVersion;

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
    InputStream getTarInputStream();

    @CheckForNull
    AuthConfigurations getBuildAuthConfigs();

    // getters

    /**
     * "t" in API
     */
    @CheckForNull
    String getTag();

    /**
     * "remote" in API
     */
    @CheckForNull
    URI getRemote();

    /**
     * "nocache" in API
     */
    @CheckForNull
    Boolean hasNoCacheEnabled();

    /**
     * "rm" in API
     */
    @CheckForNull
    Boolean hasRemoveEnabled();

    /**
     * "forcerm" in API
     */
    @CheckForNull
    Boolean isForcerm();

    /**
     * "q" in API
     */
    @CheckForNull
    Boolean isQuiet();

    /**
     * "pull" in API
     */
    @CheckForNull
    Boolean hasPullEnabled();

    @CheckForNull
    String getPathToDockerfile();

    @CheckForNull
    Long getMemory();

    @CheckForNull
    Long getMemswap();

    @CheckForNull
    String getCpushares();

    @CheckForNull
    String getCpusetcpus();

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    @CheckForNull
    Map<String, String> getBuildArgs();

    /**
     *@since {@link RemoteApiVersion#VERSION_1_22}
     */
    @CheckForNull
    Long getShmsize();

    /**
     * @since {@link RemoteApiVersion#VERSION_1_23}
     */
    @CheckForNull
    Map<String, String> getLabels();

    // setters

    BuildImageCmd withTag(String tag);

    BuildImageCmd withRemote(URI remote);

    BuildImageCmd withBaseDirectory(File baseDirectory);

    BuildImageCmd withDockerfile(File dockerfile);

    BuildImageCmd withNoCache(Boolean noCache);

    BuildImageCmd withRemove(Boolean rm);

    BuildImageCmd withForcerm(Boolean forcerm);

    BuildImageCmd withQuiet(Boolean quiet);

    BuildImageCmd withPull(Boolean pull);

    BuildImageCmd withMemory(Long memory);

    BuildImageCmd withMemswap(Long memswap);

    BuildImageCmd withCpushares(String cpushares);

    BuildImageCmd withCpusetcpus(String cpusetcpus);

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    BuildImageCmd withBuildArg(String key, String value);

    // setters lib specific

    BuildImageCmd withBuildAuthConfigs(AuthConfigurations authConfig);

    BuildImageCmd withTarInputStream(@Nonnull InputStream tarInputStream);

    /**
    *@since {@link RemoteApiVersion#VERSION_1_22}
    */
    BuildImageCmd withShmsize(Long shmsize);

    /**
    *@since {@link RemoteApiVersion#VERSION_1_23}
    */
    BuildImageCmd withLabels(Map<String, String> labels);

    interface Exec extends DockerCmdAsyncExec<BuildImageCmd, BuildResponseItem> {
    }

}
