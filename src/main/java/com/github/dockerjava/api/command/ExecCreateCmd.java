package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface ExecCreateCmd extends SyncDockerCmd<ExecCreateCmdResponse> {

    @CheckForNull
    public String getContainerId();

    @CheckForNull
    public Boolean hasAttachStderrEnabled();

    @CheckForNull
    public Boolean hasAttachStdinEnabled();

    @CheckForNull
    public Boolean hasAttachStdoutEnabled();

    @CheckForNull
    public Boolean hasTtyEnabled();

    public ExecCreateCmd withAttachStderr(Boolean attachStderr);

    public ExecCreateCmd withAttachStdin(Boolean attachStdin);

    public ExecCreateCmd withAttachStdout(Boolean attachStdout);

    public ExecCreateCmd withCmd(String... cmd);

    public ExecCreateCmd withContainerId(@Nonnull String containerId);

    public ExecCreateCmd withTty(Boolean tty);

    public static interface Exec extends DockerCmdSyncExec<ExecCreateCmd, ExecCreateCmdResponse> {
    }

}
