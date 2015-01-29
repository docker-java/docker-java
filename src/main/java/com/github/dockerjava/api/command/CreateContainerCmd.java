package com.github.dockerjava.api.command;

import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.Capability;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.VolumesFrom;

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

	public VolumesFrom[] getVolumesFrom();

	public CreateContainerCmd withVolumesFrom(VolumesFrom... volumesFrom);
	
	public HostConfig getHostConfig();
	
	public CreateContainerCmd withHostConfig(HostConfig hostConfig);

	public Capability[] getCapAdd();

	/**
	 * Add linux <a
	 * href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel
	 * capability</a> to the container. For example: adding {@link Capability#MKNOD}
	 * allows the container to create special files using the 'mknod' command.
	 */
	public CreateContainerCmd withCapAdd(Capability... capAdd);

	public Capability[] getCapDrop();

	/**
	 * Drop linux <a
	 * href="http://man7.org/linux/man-pages/man7/capabilities.7.html">kernel
	 * capability</a> from the container. For example: dropping {@link Capability#CHOWN}
	 * prevents the container from changing the owner of any files.
	 */
	public CreateContainerCmd withCapDrop(Capability... capDrop);
	
	
	public String[] getEntrypoint();
	
	public CreateContainerCmd withEntrypoint(String... entrypoint);
	
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
