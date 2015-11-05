package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.ExposedPorts;
import com.github.dockerjava.api.model.Volumes;

/**
 *
 * Create a new image from a container's changes. Returns the new image ID.
 *
 */
public interface CommitCmd extends SyncDockerCmd<String> {

    @CheckForNull
    public String getAuthor();

    @CheckForNull
    public String getContainerId();

    @CheckForNull
    public String[] getEnv();

    @CheckForNull
    public ExposedPorts getExposedPorts();

    @CheckForNull
    public String getHostname();

    @CheckForNull
    public Integer getMemory();

    @CheckForNull
    public Integer getMemorySwap();

    @CheckForNull
    public String getMessage();

    @CheckForNull
    public String[] getPortSpecs();

    @CheckForNull
    public String getRepository();

    @CheckForNull
    public String getTag();

    @CheckForNull
    public String getUser();

    @CheckForNull
    public Volumes getVolumes();

    @CheckForNull
    public String getWorkingDir();

    @CheckForNull
    public Boolean hasPauseEnabled();

    @CheckForNull
    public Boolean isOpenStdin();

    @CheckForNull
    public Boolean isStdinOnce();

    @CheckForNull
    public Boolean isTty();

    public CommitCmd withAttachStderr(Boolean attachStderr);

    public CommitCmd withAttachStdin(Boolean attachStdin);

    public CommitCmd withAttachStdout(Boolean attachStdout);

    public CommitCmd withAuthor(String author);

    public CommitCmd withCmd(String... cmd);

    public CommitCmd withContainerId(@Nonnull String containerId);

    public CommitCmd withDisableNetwork(Boolean disableNetwork);

    public CommitCmd withEnv(String... env);

    public CommitCmd withExposedPorts(ExposedPorts exposedPorts);

    public CommitCmd withHostname(String hostname);

    public CommitCmd withMemory(Integer memory);

    public CommitCmd withMemorySwap(Integer memorySwap);

    public CommitCmd withMessage(String message);

    public CommitCmd withOpenStdin(Boolean openStdin);

    public CommitCmd withPause(Boolean pause);

    public CommitCmd withPortSpecs(String... portSpecs);

    public CommitCmd withRepository(String repository);

    public CommitCmd withStdinOnce(Boolean stdinOnce);

    public CommitCmd withTag(String tag);

    public CommitCmd withTty(Boolean tty);

    public CommitCmd withUser(String user);

    public CommitCmd withVolumes(Volumes volumes);

    public CommitCmd withWorkingDir(String workingDir);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public String exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<CommitCmd, String> {
    }
}