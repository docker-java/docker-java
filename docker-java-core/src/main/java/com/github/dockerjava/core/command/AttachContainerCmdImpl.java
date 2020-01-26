package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.command.AttachContainerSpec;
import com.github.dockerjava.api.model.Frame;

/**
 * Attach to container.
 */
public class AttachContainerCmdImpl extends AbstrAsyncDockerCmd<AttachContainerCmd, Frame>
        implements AttachContainerCmd {

    private AttachContainerSpec request;

    public AttachContainerCmdImpl(AttachContainerCmd.Exec exec, String containerId) {
        super(exec);
        this.request = AttachContainerSpec.builder()
                .containerId(containerId)
                .build();
    }

    @Override
    public AttachContainerCmd fromSpec(AttachContainerSpec spec) {
        this.request = spec;
        return this;
    }

    @Override
    public String getContainerId() {
        return request.getContainerId();
    }

    @Override
    public Boolean hasLogsEnabled() {
        return request.hasLogsEnabled();
    }

    @Override
    public Boolean hasFollowStreamEnabled() {
        return request.hasFollowStreamEnabled();
    }

    @Override
    public Boolean hasTimestampsEnabled() {
        return request.hasTimestampsEnabled();
    }

    @Override
    public Boolean hasStdoutEnabled() {
        return request.hasStdoutEnabled();
    }

    @Override
    public Boolean hasStderrEnabled() {
        return request.hasStderrEnabled();
    }

    @Override
    public InputStream getStdin() {
        return request.getStdin();
    }

    @Override
    public AttachContainerCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.request = request.withContainerId(containerId);
        return this;
    }

    @Override
    public AttachContainerCmd withFollowStream(Boolean followStream) {
        this.request = request.withHasFollowStreamEnabled(followStream);
        return this;
    }

    @Override
    public AttachContainerCmd withTimestamps(Boolean timestamps) {
        this.request = request.withHasTimestampsEnabled(timestamps);
        return this;
    }

    @Override
    public AttachContainerCmd withStdOut(Boolean stdout) {
        this.request = request.withHasStdoutEnabled(stdout);
        return this;
    }

    @Override
    public AttachContainerCmd withStdErr(Boolean stderr) {
        this.request = request.withHasStderrEnabled(stderr);
        return this;
    }

    @Override
    public AttachContainerCmd withStdIn(InputStream stdin) {
        checkNotNull(stdin, "stdin was not specified");
        this.request = request.withStdin(stdin);
        return this;
    }

    @Override
    public AttachContainerCmd withLogs(Boolean logs) {
        this.request = request.withHasLogsEnabled(logs);
        return this;
    }
}
