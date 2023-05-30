package com.github.dockerjava.core.command;

import java.io.InputStream;
import java.util.Objects;

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
        this.containerId = Objects.requireNonNull(containerId, "containerId was not specified");
        return this;
    }

    @Override
    public CopyArchiveFromContainerCmdImpl withResource(String resource) {
        this.resource = Objects.requireNonNull(resource, "resource was not specified");
        return this;
    }

    @Override
    public String getHostPath() {
        return hostPath;
    }

    @Override
    public CopyArchiveFromContainerCmdImpl withHostPath(String hostPath) {
        this.hostPath = Objects.requireNonNull(hostPath, "hostPath was not specified");
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
