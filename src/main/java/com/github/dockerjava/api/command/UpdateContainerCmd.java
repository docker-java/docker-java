package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.UpdateContainerResponse;

import javax.annotation.CheckForNull;

/**
 * @author Kanstantsin Shautsou
 */
public interface UpdateContainerCmd extends SyncDockerCmd<UpdateContainerResponse> {
    @CheckForNull
    String getContainerId();

    @CheckForNull
    public Integer getBlkioWeight();

    public UpdateContainerCmd withBlkioWeight(Integer blkioWeight);

    public UpdateContainerCmd withContainerId(String containerId);

    @CheckForNull
    public Integer getCpuPeriod();

    public UpdateContainerCmd withCpuPeriod(Integer cpuPeriod);

    @CheckForNull
    public Integer getCpuQuota();

    public UpdateContainerCmd withCpuQuota(Integer cpuQuota);

    @CheckForNull
    public String getCpusetCpus();

    public UpdateContainerCmd withCpusetCpus(String cpusetCpus);

    @CheckForNull
    public String getCpusetMems();

    public UpdateContainerCmd withCpusetMems(String cpusetMems);

    @CheckForNull
    public Integer getCpuShares();

    public UpdateContainerCmd withCpuShares(Integer cpuShares);

    @CheckForNull
    public Long getKernelMemory();

    public UpdateContainerCmd withKernelMemory(Long kernelMemory);

    @CheckForNull
    public Long getMemory();

    public UpdateContainerCmd withMemory(Long memory);

    @CheckForNull
    public Long getMemoryReservation();

    public UpdateContainerCmd withMemoryReservation(Long memoryReservation);

    @CheckForNull
    Long getMemorySwap();

    UpdateContainerCmd withMemorySwap(Long memorySwap);

    interface Exec extends DockerCmdSyncExec<UpdateContainerCmd, UpdateContainerResponse> {
    }
}
