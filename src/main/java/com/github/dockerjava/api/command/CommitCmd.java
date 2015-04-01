package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.ExposedPorts;
import com.github.dockerjava.api.model.Volumes;

/**
*
* Create a new image from a container's changes. Returns the new image ID.
*
*/
public interface CommitCmd extends DockerCmd<String>{

	public String getContainerId();
	
	public CommitCmd withContainerId(String containerId);

	public String getRepository();

	public String getTag();

	public String getMessage();

	public String getAuthor();

	public boolean hasPauseEnabled();

	public CommitCmd withAttachStderr(boolean attachStderr);

	public CommitCmd withAttachStderr();

	public CommitCmd withAttachStdin(boolean attachStdin);

	public CommitCmd withAttachStdin();

	public CommitCmd withAttachStdout(boolean attachStdout);

	public CommitCmd withAttachStdout();

	public CommitCmd withCmd(String... cmd);

	public CommitCmd withDisableNetwork(boolean disableNetwork);

	public CommitCmd withAuthor(String author);

	public CommitCmd withMessage(String message);

	public CommitCmd withTag(String tag);

	public CommitCmd withRepository(String repository);

	public CommitCmd withPause(boolean pause);

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

	public boolean isOpenStdin();

	public CommitCmd withOpenStdin(boolean openStdin);

	public String[] getPortSpecs();

	public CommitCmd withPortSpecs(String... portSpecs);

	public boolean isStdinOnce();

	public CommitCmd withStdinOnce(boolean stdinOnce);

	public CommitCmd withStdinOnce();

	public boolean isTty();

	public CommitCmd withTty(boolean tty);

	public CommitCmd withTty();

	public String getUser();

	public CommitCmd withUser(String user);

	public Volumes getVolumes();

	public CommitCmd withVolumes(Volumes volumes);

	public String getWorkingDir();

	public CommitCmd withWorkingDir(String workingDir);

	/**
	 * @throws NotFoundException No such container
	 */
	@Override
	public String exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<CommitCmd, String> {
	}

}