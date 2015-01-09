package com.github.dockerjava.api.command;

public interface ExecCreateCmd extends DockerCmd<ExecCreateCmdResponce> {

    public String getContainerId();

    public ExecCreateCmd withCmd(String... cmd);

    public ExecCreateCmd attachStdin(boolean attachStdin);

    public ExecCreateCmd attachStdout(boolean attachStdout);

    public ExecCreateCmd attachStderr(boolean attachStderr);

    public ExecCreateCmd tty(boolean tty);

    public static interface Exec extends DockerCmdExec<ExecCreateCmd, ExecCreateCmdResponce> {
    }
}
