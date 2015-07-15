package com.github.dockerjava.api.command;

import java.io.InputStream;

public interface CreateImageCmd extends SyncDockerCmd<CreateImageResponse> {

    public String getRepository();

    // TODO remove method
    public String getTag();

    public InputStream getImageStream();

    /**
     * @param repository
     *            the repository to import to
     */
    public CreateImageCmd withRepository(String repository);

    /**
     * @param imageStream
     *            the InputStream of the tar file
     */
    public CreateImageCmd withImageStream(InputStream imageStream);

    /**
     * @param tag
     *            any tag for this image
     * @deprecated use repo:tag format for repository
     */
    public CreateImageCmd withTag(String tag);

    public static interface Exec extends DockerCmdSyncExec<CreateImageCmd, CreateImageResponse> {
    }

}