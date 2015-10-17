package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CopyFileFromContainerCmd;

/**
 * Copy files or folders from a container.
 */
public class CopyFileFromContainerCmdImpl extends AbstrDockerCmd<CopyFileFromContainerCmd, InputStream> implements
        CopyFileFromContainerCmd {

    private String containerId;

    private String hostPath = ".";

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
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public CopyFileFromContainerCmdImpl withResource(String resource) {
        checkNotNull(resource, "resource was not specified");
        this.resource = resource;
        return this;
    }

    @Override
    public String getHostPath() {
        return hostPath;
    }

    @Override
    public CopyFileFromContainerCmdImpl withHostPath(String hostPath) {
        checkNotNull(hostPath, "hostPath was not specified");
        this.hostPath = hostPath;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("cp ").append(hostPath).append(" ").append(containerId).append(":").append(resource).toString();
    }

    /**
     * @throws NotFoundException No such container
     */
    @Override
    public InputStream exec() throws NotFoundException {
        return super.exec();
    }
}
