package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.UpdateContainerResponse;
import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * @author Kanstantsin Shautsou
 * @since {@link RemoteApiVersion#VERSION_1_22}
 */
public interface UpdateContainerCmd extends SyncDockerCmd<UpdateContainerResponse> {
    @CheckForNull
    String getContainerId();

    @CheckForNull
    Integer getBlkioWeight();

    UpdateContainerCmd withBlkioWeight(Integer blkioWeight);

    UpdateContainerCmd withContainerId(@Nonnull String containerId);

    @CheckForNull
    Integer getCpuPeriod();

    UpdateContainerCmd withCpuPeriod(Integer cpuPeriod);

    @CheckForNull
    Integer getCpuQuota();

    UpdateContainerCmd withCpuQuota(Integer cpuQuota);

    @CheckForNull
    String getCpusetCpus();

    UpdateContainerCmd withCpusetCpus(String cpusetCpus);

    @CheckForNull
    String getCpusetMems();

    UpdateContainerCmd withCpusetMems(String cpusetMems);

    @CheckForNull
    Integer getCpuShares();

    UpdateContainerCmd withCpuShares(Integer cpuShares);

    @CheckForNull
    Long getKernelMemory();

    UpdateContainerCmd withKernelMemory(Long kernelMemory);

    @CheckForNull
    Long getMemory();

    UpdateContainerCmd withMemory(Long memory);

    @CheckForNull
    Long getMemoryReservation();

    UpdateContainerCmd withMemoryReservation(Long memoryReservation);

    @CheckForNull
    Long getMemorySwap();

    UpdateContainerCmd withMemorySwap(Long memorySwap);

    interface Exec extends DockerCmdSyncExec<UpdateContainerCmd, UpdateContainerResponse> {
    }
}
