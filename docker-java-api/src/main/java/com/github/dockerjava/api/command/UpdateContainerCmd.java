package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.BlkioRateDevice;
import com.github.dockerjava.api.model.BlkioWeightDevice;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.DeviceRequest;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.api.model.Ulimit;
import com.github.dockerjava.api.model.UpdateContainerResponse;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Kanstantsin Shautsou
 * @since {@link RemoteApiVersion#VERSION_1_22}
 */
public interface UpdateContainerCmd extends SyncDockerCmd<UpdateContainerResponse> {
    @CheckForNull
    String getContainerId();

    UpdateContainerCmd withContainerId(@Nonnull String containerId);

    @CheckForNull
    Integer getBlkioWeight();

    UpdateContainerCmd withBlkioWeight(Integer blkioWeight);

    @CheckForNull
    List<BlkioWeightDevice> getBlkioWeightDevice();

    UpdateContainerCmd withBlkioWeightDevice(List<BlkioWeightDevice> blkioWeightDevice);

    @CheckForNull
    List<BlkioRateDevice> getBlkioDeviceReadBps();

    UpdateContainerCmd withBlkioDeviceReadBps(List<BlkioRateDevice> blkioDeviceReadBps);

    @CheckForNull
    List<BlkioRateDevice> getBlkioDeviceWriteBps();

    UpdateContainerCmd withBlkioDeviceWriteBps(List<BlkioRateDevice> blkioDeviceWriteBps);

    @CheckForNull
    List<BlkioRateDevice> getBlkioDeviceReadIOps();

    UpdateContainerCmd withBlkioDeviceReadIOps(List<BlkioRateDevice> blkioDeviceReadIOps);

    @CheckForNull
    List<BlkioRateDevice> getBlkioDeviceWriteIOps();

    UpdateContainerCmd withBlkioDeviceWriteIOps(List<BlkioRateDevice> blkioDeviceWriteIOps);

    @CheckForNull
    Long getCpuPeriod();

    UpdateContainerCmd withCpuPeriod(Long cpuPeriod);

    @CheckForNull
    Long getCpuQuota();

    UpdateContainerCmd withCpuQuota(Long cpuQuota);

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
    Long getCpuRealtimePeriod();

    UpdateContainerCmd withCpuRealtimePeriod(Long cpuRealtimePeriod);

    @CheckForNull
    Long getCpuRealtimeRuntime();

    UpdateContainerCmd withCpuRealtimeRuntime(Long cpuRealtimeRuntime);

    @CheckForNull
    List<Device> getDevices();

    UpdateContainerCmd withDevices(List<Device> devices);

    @CheckForNull
    List<String> getDeviceCgroupRules();

    UpdateContainerCmd withDeviceCgroupRules(List<String> deviceCgroupRules);

    @CheckForNull
    List<DeviceRequest> getDeviceRequests();

    UpdateContainerCmd withDeviceRequests(List<DeviceRequest> deviceRequests);

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

    @CheckForNull
    Long getNanoCPUs();

    UpdateContainerCmd withNanoCPUs(Long nanoCPUs);

    @CheckForNull
    Boolean getOomKillDisable();

    UpdateContainerCmd withOomKillDisable(Boolean oomKillDisable);

    @CheckForNull
    Boolean getInit();

    UpdateContainerCmd withInit(Boolean init);

    @CheckForNull
    Long getPidsLimit();

    UpdateContainerCmd withPidsLimit(Long pidsLimit);

    @CheckForNull
    List<Ulimit> getUlimits();

    UpdateContainerCmd withUlimits(List<Ulimit> ulimits);

    @CheckForNull
    RestartPolicy getRestartPolicy();

    UpdateContainerCmd withRestartPolicy(RestartPolicy restartPolicy);

    interface Exec extends DockerCmdSyncExec<UpdateContainerCmd, UpdateContainerResponse> {
    }
}
