package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.DockerCmdAsyncExec;
import com.github.dockerjava.api.command.LogSwarmObjectCmd;
import com.github.dockerjava.api.model.Frame;

import javax.annotation.Nonnull;

public class LogSwarmObjectImpl extends AbstrAsyncDockerCmd<LogSwarmObjectCmd, Frame> implements LogSwarmObjectCmd {
    private String id;
    private Boolean followStream;
    private Boolean timestamps;
    private Boolean stdout;
    private Boolean stderr;
    private Boolean tailAll;
    private Boolean follow;
    private Integer tail;
    private Integer since;
    private Boolean details;

    public LogSwarmObjectImpl(DockerCmdAsyncExec<LogSwarmObjectCmd, Frame> execution, String id) {
        super(execution);
        this.id = id;
    }

    @Override
    public LogSwarmObjectImpl withId(@Nonnull String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public Boolean getFollowStream() {
        return followStream;
    }

    public LogSwarmObjectImpl withFollowStream(Boolean followStream) {
        this.followStream = followStream;
        return this;
    }

    public Boolean getTimestamps() {
        return timestamps;
    }

    @Override
    public LogSwarmObjectImpl withTimestamps(Boolean timestamps) {
        this.timestamps = timestamps;
        return this;
    }

    public Boolean getStdout() {
        return stdout;
    }

    public LogSwarmObjectImpl withStdout(Boolean stdout) {
        this.stdout = stdout;
        return this;
    }

    public Boolean getStderr() {
        return stderr;
    }

    public LogSwarmObjectImpl withStderr(Boolean stderr) {
        this.stderr = stderr;
        return this;
    }

    public Boolean getTailAll() {
        return tailAll;
    }

    public LogSwarmObjectImpl withTailAll(Boolean tailAll) {
        this.tailAll = tailAll;
        return this;
    }

    public Integer getTail() {
        return tail;
    }

    @Override
    public LogSwarmObjectImpl withTail(Integer tail) {
        this.tail = tail;
        return this;
    }

    public Integer getSince() {
        return since;
    }

    @Override
    public LogSwarmObjectImpl withSince(Integer since) {
        this.since = since;
        return this;
    }

    public Boolean getFollow() {
        return follow;
    }

    @Override
    public LogSwarmObjectImpl withFollow(Boolean follow) {
        this.follow = follow;
        return this;
    }

    @Override
    public LogSwarmObjectCmd withDetails(Boolean details) {
        this.details = details;
        return this;
    }

    public Boolean getDetails() {
        return details;
    }
}
