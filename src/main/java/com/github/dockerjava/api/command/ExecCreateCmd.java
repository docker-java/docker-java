package com.github.dockerjava.api.command;

public interface ExecCreateCmd extends SyncDockerCmd<ExecCreateCmdResponse> {

    public String getContainerId();

    public ExecCreateCmd withContainerId(String containerId);

    public ExecCreateCmd withCmd(String... cmd);

    public ExecCreateCmd withAttachStdin(Boolean attachStdin);

    public ExecCreateCmd withAttachStdin();

    public Boolean hasAttachStdinEnabled();

    public ExecCreateCmd withAttachStdout(Boolean attachStdout);

    public ExecCreateCmd withAttachStdout();

    public Boolean hasAttachStdoutEnabled();

    public ExecCreateCmd withAttachStderr(Boolean attachStderr);

    public ExecCreateCmd withAttachStderr();

    public Boolean hasAttachStderrEnabled();

    public ExecCreateCmd withTty(Boolean tty);

    public ExecCreateCmd withTty();

    public Boolean hasTtyEnabled();

    public static interface Exec extends DockerCmdSyncExec<ExecCreateCmd, ExecCreateCmdResponse> {
    }
}
