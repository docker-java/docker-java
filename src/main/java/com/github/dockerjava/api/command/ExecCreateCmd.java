package com.github.dockerjava.api.command;

public interface ExecCreateCmd extends DockerCmd<ExecCreateCmdResponse> {

    public String getContainerId();
    
    public ExecCreateCmd withContainerId(String containerId);

    public ExecCreateCmd withCmd(String... cmd);

    public ExecCreateCmd withAttachStdin(boolean attachStdin);
    
    public ExecCreateCmd withAttachStdin();
    
    public boolean hasAttachStdinEnabled();

    public ExecCreateCmd withAttachStdout(boolean attachStdout);
    
    public ExecCreateCmd withAttachStdout();
    
    public boolean hasAttachStdoutEnabled();

    public ExecCreateCmd withAttachStderr(boolean attachStderr);
    
    public ExecCreateCmd withAttachStderr();
    
    public boolean hasAttachStderrEnabled();

    public ExecCreateCmd withTty(boolean tty);
    
    public ExecCreateCmd withTty();
    
    public boolean hasTtyEnabled();

    public static interface Exec extends DockerCmdExec<ExecCreateCmd, ExecCreateCmdResponse> {
    }
}
