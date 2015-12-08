package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import com.github.dockerjava.api.command.CopyArchiveFromContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Copy files or folders from a container.
 */
public class CopyArchiveFromContainerCmdImpl extends AbstrDockerCmd<CopyArchiveFromContainerCmd, InputStream> implements
        CopyArchiveFromContainerCmd {

    private String containerId;

    private String hostPath = ".";

    private String resource;

    public CopyArchiveFromContainerCmdImpl(CopyArchiveFromContainerCmd.Exec exec, String containerId, String resource) {
        super(exec);
        withContainerId(containerId);
        withResource(resource);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public CopyArchiveFromContainerCmdImpl withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public CopyArchiveFromContainerCmdImpl withResource(String resource) {
        checkNotNull(resource, "resource was not specified");
        this.resource = resource;
        return this;
    }

    @Override
    public String getHostPath() {
        return hostPath;
    }

    @Override
    public CopyArchiveFromContainerCmdImpl withHostPath(String hostPath) {
        checkNotNull(hostPath, "hostPath was not specified");
        this.hostPath = hostPath;
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public InputStream exec() throws NotFoundException {
        return super.exec();
    }
}
