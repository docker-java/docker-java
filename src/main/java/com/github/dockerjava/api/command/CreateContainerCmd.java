package com.github.dockerjava.api.command;

import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Volume;

public interface CreateContainerCmd extends DockerCmd<CreateContainerResponse>{

	public CreateContainerCmd withName(String name);

	public String getName();

	public CreateContainerCmd withExposedPorts(ExposedPort... exposedPorts);

	public ExposedPort[] getExposedPorts();

	public boolean isDisableNetwork();

	public String getWorkingDir();

	public CreateContainerCmd withWorkingDir(String workingDir);

	public String getHostName();

	public CreateContainerCmd withDisableNetwork(boolean disableNetwork);

	public CreateContainerCmd withHostName(String hostName);

	public String[] getPortSpecs();

	public CreateContainerCmd withPortSpecs(String... portSpecs);

	public String getUser();

	public CreateContainerCmd withUser(String user);

	public boolean isTty();

	public CreateContainerCmd withTty(boolean tty);

	public boolean isStdinOpen();

	public CreateContainerCmd withStdinOpen(boolean stdinOpen);

	public boolean isStdInOnce();

	public CreateContainerCmd withStdInOnce(boolean stdInOnce);

	public long getMemoryLimit();

	public CreateContainerCmd withMemoryLimit(long memoryLimit);

	public long getMemorySwap();

	public CreateContainerCmd withMemorySwap(long memorySwap);

	public int getCpuShares();

	public CreateContainerCmd withCpuShares(int cpuShares);

	public boolean isAttachStdin();

	public CreateContainerCmd withAttachStdin(boolean attachStdin);

	public boolean isAttachStdout();

	public CreateContainerCmd withAttachStdout(boolean attachStdout);

	public boolean isAttachStderr();

	public CreateContainerCmd withAttachStderr(boolean attachStderr);

	public String[] getEnv();

	public CreateContainerCmd withEnv(String... env);

	public String[] getCmd();

	public CreateContainerCmd withCmd(String... cmd);

	public String[] getDns();

	public CreateContainerCmd withDns(String... dns);

	public String getImage();

	public CreateContainerCmd withImage(String image);

	public Volume[] getVolumes();

	public CreateContainerCmd withVolumes(Volume... volumes);

	public String[] getVolumesFrom();

	public CreateContainerCmd withVolumesFrom(String... volumesFrom);


	/**
	 * @throws NotFoundException No such container
	 * @throws ConflictException Named container already exists
	 */
    @Override
	public CreateContainerResponse exec() throws NotFoundException,
			ConflictException;
	
	public static interface Exec extends DockerCmdExec<CreateContainerCmd, CreateContainerResponse> {
	}

}
