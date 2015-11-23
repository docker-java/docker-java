package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;

public interface CopyArchiveToContainerCmd extends SyncDockerCmd<Void> {

    public String getContainerId();

    public String getHostResource();

    public boolean isNoOverwriteDirNonDir();

    public boolean isDirChildrenOnly();

    /**
     * Set container's id
     *
     * @param containerId id of the container to copy file to
     * @return this
     */
    public CopyArchiveToContainerCmd withContainerId(String containerId);

    /**
     * Set path to the resource on the host machine
     *
     * @param resource path to the resource on the host machine
     * @return this
     */
    public CopyArchiveToContainerCmd withHostResource(String resource);

    /**
     * If set to true then it will be an error if unpacking the given content would cause an existing directory to be replaced with a non-directory and vice versa
     *
     * @param noOverwriteDirNonDir flag to know if non directory can be overwritten
     * @return this
     */
    public CopyArchiveToContainerCmd withNoOverwriteDirNonDir(boolean noOverwriteDirNonDir);

    /**
     * If this flag is set to true, all children of the local directory will be copied to the remote without the root directory.
     * For ex: if I have root/titi and root/tata and the remote path is /var/data.
     * dirChildrenOnly = true will create /var/data/titi and /var/data/tata
     * dirChildrenOnly = false will create /var/data/root/titi and /var/data/root/tata
     *
     * @param dirChildrenOnly if root directory is ignored
     * @return this
     */
    public CopyArchiveToContainerCmd withDirChildrenOnly(boolean dirChildrenOnly);

    public String getRemotePath();

    public CopyArchiveToContainerCmd withRemotePath(String remotePath);

    @Override
    public Void exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<CopyArchiveToContainerCmd, Void> {
    }

}
