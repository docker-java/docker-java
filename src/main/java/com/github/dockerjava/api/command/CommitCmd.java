package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ExposedPorts;
import com.github.dockerjava.api.model.Volumes;

/**
 *
 * Create a new image from a container's changes. Returns the new image ID.
 *
 */
public interface CommitCmd extends SyncDockerCmd<String> {

    @CheckForNull
    String getAuthor();

    @CheckForNull
    String getContainerId();

    @CheckForNull
    String[] getEnv();

    @CheckForNull
    ExposedPorts getExposedPorts();

    @CheckForNull
    String getHostname();

    @CheckForNull
    Integer getMemory();

    @CheckForNull
    Integer getMemorySwap();

    @CheckForNull
    String getMessage();

    @CheckForNull
    String[] getPortSpecs();

    @CheckForNull
    String getRepository();

    @CheckForNull
    String getTag();

    @CheckForNull
    String getUser();

    @CheckForNull
    Volumes getVolumes();

    @CheckForNull
    String getWorkingDir();

    @CheckForNull
    Boolean hasPauseEnabled();

    @CheckForNull
    Boolean isOpenStdin();

    @CheckForNull
    Boolean isStdinOnce();

    @CheckForNull
    Boolean isTty();

    CommitCmd withAttachStderr(Boolean attachStderr);

    CommitCmd withAttachStdin(Boolean attachStdin);

    CommitCmd withAttachStdout(Boolean attachStdout);

    CommitCmd withAuthor(String author);

    CommitCmd withCmd(String... cmd);

    CommitCmd withContainerId(@Nonnull String containerId);

    CommitCmd withDisableNetwork(Boolean disableNetwork);

    CommitCmd withEnv(String... env);

    CommitCmd withExposedPorts(ExposedPorts exposedPorts);

    CommitCmd withHostname(String hostname);

    CommitCmd withMemory(Integer memory);

    CommitCmd withMemorySwap(Integer memorySwap);

    CommitCmd withMessage(String message);

    CommitCmd withOpenStdin(Boolean openStdin);

    CommitCmd withPause(Boolean pause);

    CommitCmd withPortSpecs(String... portSpecs);

    CommitCmd withRepository(String repository);

    CommitCmd withStdinOnce(Boolean stdinOnce);

    CommitCmd withTag(String tag);

    CommitCmd withTty(Boolean tty);

    CommitCmd withUser(String user);

    CommitCmd withVolumes(Volumes volumes);

    CommitCmd withWorkingDir(String workingDir);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    String exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<CommitCmd, String> {
    }
}
