package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.ExposedPorts;
import com.github.dockerjava.api.model.Volumes;

/**
 *
 * Create a new image from a container's changes. Returns the new image ID.
 *
 */
public interface CommitCmd extends SyncDockerCmd<String> {

    public String getContainerId();

    public CommitCmd withContainerId(String containerId);

    public String getRepository();

    public String getTag();

    public String getMessage();

    public String getAuthor();

    public Boolean hasPauseEnabled();

    public CommitCmd withAttachStderr(Boolean attachStderr);

    public CommitCmd withAttachStderr();

    public CommitCmd withAttachStdin(Boolean attachStdin);

    public CommitCmd withAttachStdin();

    public CommitCmd withAttachStdout(Boolean attachStdout);

    public CommitCmd withAttachStdout();

    public CommitCmd withCmd(String... cmd);

    public CommitCmd withDisableNetwork(Boolean disableNetwork);

    public CommitCmd withAuthor(String author);

    public CommitCmd withMessage(String message);

    public CommitCmd withTag(String tag);

    public CommitCmd withRepository(String repository);

    public CommitCmd withPause(Boolean pause);

    public String[] getEnv();

    public CommitCmd withEnv(String... env);

    public ExposedPorts getExposedPorts();

    public CommitCmd withExposedPorts(ExposedPorts exposedPorts);

    public String getHostname();

    public CommitCmd withHostname(String hostname);

    public Integer getMemory();

    public CommitCmd withMemory(Integer memory);

    public Integer getMemorySwap();

    public CommitCmd withMemorySwap(Integer memorySwap);

    public Boolean isOpenStdin();

    public CommitCmd withOpenStdin(Boolean openStdin);

    public String[] getPortSpecs();

    public CommitCmd withPortSpecs(String... portSpecs);

    public Boolean isStdinOnce();

    public CommitCmd withStdinOnce(Boolean stdinOnce);

    public CommitCmd withStdinOnce();

    public Boolean isTty();

    public CommitCmd withTty(Boolean tty);

    public CommitCmd withTty();

    public String getUser();

    public CommitCmd withUser(String user);

    public Volumes getVolumes();

    public CommitCmd withVolumes(Volumes volumes);

    public String getWorkingDir();

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