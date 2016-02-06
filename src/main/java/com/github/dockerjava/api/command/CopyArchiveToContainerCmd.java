package com.github.dockerjava.api.command;

import java.io.InputStream;

import com.github.dockerjava.api.exception.NotFoundException;

public interface CopyArchiveToContainerCmd extends SyncDockerCmd<Void> {

    String getContainerId();

    String getHostResource();

    InputStream getTarInputStream();

    boolean isNoOverwriteDirNonDir();

    boolean isDirChildrenOnly();

    /**
     * Set container's id
     *
     * @param containerId
     *            id of the container to copy file to
     */
    CopyArchiveToContainerCmd withContainerId(String containerId);

    /**
     * Set path to the resource on the host machine
     *
     * @param resource
     *            path to the resource on the host machine
     */
    CopyArchiveToContainerCmd withHostResource(String resource);

    /**
     * Set the tar input stream that will be uploaded to the container. withHostResource or withTarInputStream can be defined but not both.
     *
     * @param tarInputStream
     *            the stream to upload to the container
     */
    CopyArchiveToContainerCmd withTarInputStream(InputStream tarInputStream);

    /**
     * If set to true then it will be an error if unpacking the given content would cause an existing directory to be replaced with a
     * non-directory and vice versa
     *
     * @param noOverwriteDirNonDir
     *            flag to know if non directory can be overwritten
     */
    CopyArchiveToContainerCmd withNoOverwriteDirNonDir(boolean noOverwriteDirNonDir);

    /**
     * If this flag is set to true, all children of the local directory will be copied to the remote without the root directory. For ex: if
     * I have root/titi and root/tata and the remote path is /var/data. dirChildrenOnly = true will create /var/data/titi and /var/data/tata
     * dirChildrenOnly = false will create /var/data/root/titi and /var/data/root/tata
     *
     * @param dirChildrenOnly
     *            if root directory is ignored
     */
    CopyArchiveToContainerCmd withDirChildrenOnly(boolean dirChildrenOnly);

    String getRemotePath();

    CopyArchiveToContainerCmd withRemotePath(String remotePath);

    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<CopyArchiveToContainerCmd, Void> {
    }

}
