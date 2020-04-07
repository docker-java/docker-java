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

    private AttachContainerSpec spec;

    public AttachContainerCmdImpl(AttachContainerCmd.Exec exec, String containerId) {
        this(exec, AttachContainerSpec.of(containerId));
    }

    AttachContainerCmdImpl(AttachContainerCmd.Exec exec, AttachContainerSpec spec) {
        super(exec);
        this.spec = spec;
    }

    @Override
    public String getContainerId() {
        return spec.getContainerId();
    }

    @Override
    public Boolean hasLogsEnabled() {
        return spec.hasLogsEnabled();
    }

    @Override
    public Boolean hasFollowStreamEnabled() {
        return spec.hasFollowStreamEnabled();
    }

    @Override
    public Boolean hasTimestampsEnabled() {
        return spec.hasTimestampsEnabled();
    }

    @Override
    public Boolean hasStdoutEnabled() {
        return spec.hasStdoutEnabled();
    }

    @Override
    public Boolean hasStderrEnabled() {
        return spec.hasStderrEnabled();
    }

    @Override
    public InputStream getStdin() {
        return spec.getStdin();
    }

    @Override
    public AttachContainerCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.spec = spec.withContainerId(containerId);
        return this;
    }

    @Override
    public AttachContainerCmd withFollowStream(Boolean followStream) {
        this.spec = spec.withHasFollowStreamEnabled(followStream);
        return this;
    }

    @Override
    public AttachContainerCmd withTimestamps(Boolean timestamps) {
        this.spec = spec.withHasTimestampsEnabled(timestamps);
        return this;
    }

    @Override
    public AttachContainerCmd withStdOut(Boolean stdout) {
        this.spec = spec.withHasStdoutEnabled(stdout);
        return this;
    }

    @Override
    public AttachContainerCmd withStdErr(Boolean stderr) {
        this.spec = spec.withHasStderrEnabled(stderr);
        return this;
    }

    @Override
    public AttachContainerCmd withStdIn(InputStream stdin) {
        checkNotNull(stdin, "stdin was not specified");
        this.spec = spec.withStdin(stdin);
        return this;
    }

    @Override
    public AttachContainerCmd withLogs(Boolean logs) {
        this.spec = spec.withHasLogsEnabled(logs);
        return this;
    }
}
