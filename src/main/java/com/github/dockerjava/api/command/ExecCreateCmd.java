package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface ExecCreateCmd extends SyncDockerCmd<ExecCreateCmdResponse> {

    @CheckForNull
    String getContainerId();

    @CheckForNull
    Boolean hasAttachStderrEnabled();

    @CheckForNull
    Boolean hasAttachStdinEnabled();

    @CheckForNull
    Boolean hasAttachStdoutEnabled();

    @CheckForNull
    Boolean hasTtyEnabled();

    @CheckForNull
    String getUser();

    ExecCreateCmd withAttachStderr(Boolean attachStderr);

    ExecCreateCmd withAttachStdin(Boolean attachStdin);

    ExecCreateCmd withAttachStdout(Boolean attachStdout);

    ExecCreateCmd withCmd(String... cmd);

    ExecCreateCmd withContainerId(@Nonnull String containerId);

    ExecCreateCmd withTty(Boolean tty);

    ExecCreateCmd withUser(String user);

    interface Exec extends DockerCmdSyncExec<ExecCreateCmd, ExecCreateCmdResponse> {
    }

}
