package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface CreateExecCmd extends SyncDockerCmd<CreateExecCmdResponse> {

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

    public CreateExecCmd withAttachStderr(Boolean attachStderr);

    public CreateExecCmd withAttachStdin(Boolean attachStdin);

    public CreateExecCmd withAttachStdout(Boolean attachStdout);

    public CreateExecCmd withCmd(String... cmd);

    public CreateExecCmd withContainerId(@Nonnull String containerId);

    public CreateExecCmd withTty(Boolean tty);

    public static interface Exec extends DockerCmdSyncExec<CreateExecCmd, CreateExecCmdResponse> {
    }

}
