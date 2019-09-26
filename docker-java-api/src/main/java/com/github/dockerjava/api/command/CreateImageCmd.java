package com.github.dockerjava.api.command;

import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface CreateImageCmd extends SyncDockerCmd<CreateImageResponse> {

    @CheckForNull
    String getRepository();

    @CheckForNull
    String getTag();

    @CheckForNull
    String getPlatform();

    @CheckForNull
    InputStream getImageStream();

    /**
     * @param repository
     *            the repository to import to
     */
    CreateImageCmd withRepository(@Nonnull String repository);

    /**
     * @param imageStream
     *            the InputStream of the tar file
     */
    CreateImageCmd withImageStream(InputStream imageStream);

    /**
     * @param tag
     *            any tag for this image
     * @deprecated use repo:tag format for repository
     */
    CreateImageCmd withTag(String tag);

    /**
     * @param platform
     *            the platform for this image
     */
    CreateImageCmd withPlatform(String platform);

    interface Exec extends DockerCmdSyncExec<CreateImageCmd, CreateImageResponse> {
    }

}
