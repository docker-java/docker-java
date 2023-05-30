package com.github.dockerjava.core.command;

import java.io.InputStream;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 *
 * Copy files or folders from a container.
 *
 */
public class CopyFileFromContainerCmdImpl extends AbstrDockerCmd<CopyFileFromContainerCmd, InputStream> implements
        CopyFileFromContainerCmd {

    private String containerId;

    @JsonProperty("HostPath")
    private String hostPath = ".";

    @JsonProperty("Resource")
    private String resource;

    public CopyFileFromContainerCmdImpl(CopyFileFromContainerCmd.Exec exec, String containerId, String resource) {
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
    public CopyFileFromContainerCmdImpl withContainerId(String containerId) {
        this.containerId = Objects.requireNonNull(containerId, "containerId was not specified");
        return this;
    }

    @Override
    public CopyFileFromContainerCmdImpl withResource(String resource) {
        this.resource = Objects.requireNonNull(resource, "resource was not specified");
        return this;
    }

    @Override
    public String getHostPath() {
        return hostPath;
    }

    @Override
    public CopyFileFromContainerCmdImpl withHostPath(String hostPath) {
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
