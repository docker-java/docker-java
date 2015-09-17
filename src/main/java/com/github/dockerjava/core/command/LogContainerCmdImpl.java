package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.model.Frame;

/**
 * Get container logs
 *
 * @param followStream
 *            - true or false, return stream. Defaults to false.
 * @param stdout
 *            - true or false, includes stdout log. Defaults to false.
 * @param stderr
 *            - true or false, includes stderr log. Defaults to false.
 * @param timestamps
 *            - true or false, if true, print timestamps for every log line. Defaults to false.
 * @param tail
 *            - `all` or `<number>`, Output specified number of lines at the end of logs
 * @param since
 *            - UNIX timestamp (integer) to filter logs. Specifying a timestamp will only output log-entries since that
 *            timestamp. Default: 0 (unfiltered)
 */
public class LogContainerCmdImpl extends AbstrAsyncDockerCmd<LogContainerCmd, Frame> implements LogContainerCmd {

    private String containerId;

    private int tail = -1;

    private boolean followStream, timestamps, stdout, stderr;

    private int since = 0;

    public LogContainerCmdImpl(LogContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public int getTail() {
        return tail;
    }

    @Override
    public boolean hasFollowStreamEnabled() {
        return followStream;
    }

    @Override
    public boolean hasTimestampsEnabled() {
        return timestamps;
    }

    @Override
    public boolean hasStdoutEnabled() {
        return stdout;
    }

    @Override
    public boolean hasStderrEnabled() {
        return stderr;
    }

    @Override
    public int getSince() {
        return since;
    }

    @Override
    public LogContainerCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public LogContainerCmd withFollowStream() {
        return withFollowStream(true);
    }

    @Override
    public LogContainerCmd withFollowStream(boolean followStream) {
        this.followStream = followStream;
        return this;
    }

    @Override
    public LogContainerCmd withTimestamps() {
        return withTimestamps(true);
    }

    @Override
    public LogContainerCmd withTimestamps(boolean timestamps) {
        this.timestamps = timestamps;
        return this;
    }

    @Override
    public LogContainerCmd withStdOut() {
        return withStdOut(true);
    }

    @Override
    public LogContainerCmd withStdOut(boolean stdout) {
        this.stdout = stdout;
        return this;
    }

    @Override
    public LogContainerCmd withStdErr() {
        return withStdErr(true);
    }

    @Override
    public LogContainerCmd withStdErr(boolean stderr) {
        this.stderr = stderr;
        return this;
    }

    @Override
    public LogContainerCmd withTailAll() {
        this.tail = -1;
        return this;
    }

    @Override
    public LogContainerCmd withTail(int tail) {
        this.tail = tail;
        return this;
    }

    @Override
    public LogContainerCmd withSince(int since) {
        this.since = since;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder("logs ").append(followStream ? "--follow=true" : "")
                .append(timestamps ? "--timestamps=true" : "").append(since > 0 ? "--since=" + since : "")
                .append(containerId).toString();
    }

}
