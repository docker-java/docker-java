package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.github.dockerjava.api.NotFoundException;

public class CopyFileToContainerCmdImpl extends AbstrDockerCmd<CopyFileToContainerCmd, Void> implements CopyFileToContainerCmd {

    private String containerId;

    private String remotePath = ".";

    private String hostResource;

    private boolean noOverwriteDirNonDir = false;

    private boolean dirChildrenOnly = false;

    public CopyFileToContainerCmdImpl(CopyFileToContainerCmd.Exec exec, String containerId, String hostResource) {
        super(exec);
        withContainerId(containerId);
        withHostResource(hostResource);
    }

    @Override
    public CopyFileToContainerCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public CopyFileToContainerCmd withHostResource(String hostResource) {
        checkNotNull(hostResource, "hostResource was not specified");
        this.hostResource = hostResource;
        return this;
    }

    @Override
    public String getContainerId() {
        return this.containerId;
    }

    @Override
    public String getHostResource() {
        return this.hostResource;
    }

    @Override
    public boolean isNoOverwriteDirNonDir() {
        return this.noOverwriteDirNonDir;
    }

    @Override
    public CopyFileToContainerCmd withNoOverwriteDirNonDir(boolean noOverwriteDirNonDir) {
        this.noOverwriteDirNonDir = noOverwriteDirNonDir;
        return this;
    }

    @Override
    public CopyFileToContainerCmd withRemotePath(String remotePath) {
        checkNotNull(remotePath, "remotePath was not specified");
        this.remotePath = remotePath;
        return this;
    }

    @Override
    public String getRemotePath() {
        return this.remotePath;
    }

    @Override
    public boolean isDirChildrenOnly() {
        return this.dirChildrenOnly;
    }

    @Override
    public CopyFileToContainerCmd withDirChildrenOnly(boolean dirChildrenOnly) {
        this.dirChildrenOnly = dirChildrenOnly;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("cp ").append(hostResource).append(" ").append(containerId).append(":").append(remotePath).toString();
    }

    /**
     * @throws NotFoundException No such container
     */
    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }
}
