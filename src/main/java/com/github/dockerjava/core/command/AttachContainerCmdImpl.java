package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.model.Frame;

/**
 * Attach to container
 *
 * @param logs
 *            - true or false, includes logs. Defaults to false.
 *
 * @param followStream
 *            - true or false, return stream. Defaults to false.
 * @param stdout
 *            - true or false, includes stdout log. Defaults to false.
 * @param stderr
 *            - true or false, includes stderr log. Defaults to false.
 * @param timestamps
 *            - true or false, if true, print timestamps for every log line. Defaults to false.
 */
public class AttachContainerCmdImpl extends AbstrAsyncDockerCmd<AttachContainerCmd, Frame> implements
        AttachContainerCmd {

    private String containerId;

    private Boolean logs, followStream, timestamps, stdout, stderr;

    public AttachContainerCmdImpl(AttachContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public Boolean hasLogsEnabled() {
        return logs;
    }

    @Override
    public Boolean hasFollowStreamEnabled() {
        return followStream;
    }

    @Override
    public Boolean hasTimestampsEnabled() {
        return timestamps;
    }

    @Override
    public Boolean hasStdoutEnabled() {
        return stdout;
    }

    @Override
    public Boolean hasStderrEnabled() {
        return stderr;
    }

    @Override
    public AttachContainerCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public AttachContainerCmd withFollowStream(Boolean followStream) {
        this.followStream = followStream;
        return this;
    }

    @Override
    public AttachContainerCmd withTimestamps(Boolean timestamps) {
        this.timestamps = timestamps;
        return this;
    }

    @Override
    public AttachContainerCmd withStdOut(Boolean stdout) {
        this.stdout = stdout;
        return this;
    }

    @Override
    public AttachContainerCmd withStdErr(Boolean stderr) {
        this.stderr = stderr;
        return this;
    }

    @Override
    public AttachContainerCmd withLogs(Boolean logs) {
        this.logs = logs;
        return this;
    }
}
