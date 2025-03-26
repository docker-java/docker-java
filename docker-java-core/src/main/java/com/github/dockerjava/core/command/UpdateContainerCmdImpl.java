package com.github.dockerjava.core.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.UpdateContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.BlkioRateDevice;
import com.github.dockerjava.api.model.BlkioWeightDevice;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.DeviceRequest;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.api.model.Ulimit;
import com.github.dockerjava.api.model.UpdateContainerResponse;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Kanstantsin Shautsou
 * @see <a href="https://docs.docker.com/engine/reference/api/docker_remote_api_v1.22/">
 * https://docs.docker.com/engine/reference/api/docker_remote_api_v1.22/</a>
 * @since {@link RemoteApiVersion#VERSION_1_22}
 */
public class UpdateContainerCmdImpl extends AbstrDockerCmd<UpdateContainerCmd, UpdateContainerResponse>
        implements UpdateContainerCmd {

    @JsonIgnore
    private String containerId;

    @JsonProperty("BlkioWeight")
    private Integer blkioWeight;

    @JsonProperty("BlkioWeightDevice")
    private List<BlkioWeightDevice> blkioWeightDevice;

    @JsonProperty("BlkioDeviceReadBps")
    private List<BlkioRateDevice> blkioDeviceReadBps;

    @JsonProperty("BlkioDeviceWriteBps")
    private List<BlkioRateDevice> blkioDeviceWriteBps;

    @JsonProperty("BlkioDeviceReadIOps")
    private List<BlkioRateDevice> blkioDeviceReadIOps;

    @JsonProperty("BlkioDeviceWriteIOps")
    private List<BlkioRateDevice> blkioDeviceWriteIOps;

    @JsonProperty("CpuShares")
    private Integer cpuShares;

    @JsonProperty("CpuPeriod")
    private Long cpuPeriod;

    @JsonProperty("CpuQuota")
    private Long cpuQuota;

    @JsonProperty("CpuRealtimePeriod")
    private Long cpuRealtimePeriod;

    @JsonProperty("CpuRealtimeRuntime")
    private Long cpuRealtimeRuntime;

    @JsonProperty("NanoCpus")
    private Long nanoCPUs;

    @JsonProperty("CpusetCpus")
    private String cpusetCpus;

    @JsonProperty("CpusetMems")
    private String cpusetMems;

    @JsonProperty("Devices")
    private List<Device> devices;

    @JsonProperty("DeviceCgroupRules")
    private List<String> deviceCgroupRules;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_40}
     */
    @JsonProperty("DeviceRequests")
    private List<DeviceRequest> deviceRequests;

    @JsonProperty("Memory")
    private Long memory;

    @JsonProperty("MemorySwap")
    private Long memorySwap;

    @JsonProperty("MemoryReservation")
    private Long memoryReservation;

    @JsonProperty("KernelMemory")
    private Long kernelMemory;

    @JsonProperty("OomKillDisable")
    private Boolean oomKillDisable;

    @JsonProperty("Init")
    private Boolean init;

    @JsonProperty("PidsLimit")
    private Long pidsLimit;

    @JsonProperty("Ulimits")
    private List<Ulimit> ulimits;

    @JsonProperty("RestartPolicy")
    private RestartPolicy restartPolicy;

    public UpdateContainerCmdImpl(UpdateContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    /**
     * @see #containerId
     */
    @CheckForNull
    @JsonIgnore
    public String getContainerId() {
        return containerId;
    }

    /**
     * @see #containerId
     */
    public UpdateContainerCmd withContainerId(@Nonnull String containerId) {
        this.containerId = containerId;
        return this;
    }

    /**
     * @see #blkioWeight
     */
    @CheckForNull
    public Integer getBlkioWeight() {
        return blkioWeight;
    }

    /**
     * @see #blkioWeight
     */
    public UpdateContainerCmd withBlkioWeight(Integer blkioWeight) {
        this.blkioWeight = blkioWeight;
        return this;
    }

    /**
     * @see #blkioWeightDevice
     */
    @CheckForNull
    public List<BlkioWeightDevice> getBlkioWeightDevice() {
        return blkioWeightDevice;
    }

    public UpdateContainerCmd withBlkioWeightDevice(List<BlkioWeightDevice> blkioWeightDevice) {
        this.blkioWeightDevice = blkioWeightDevice;
        return this;
    }

    /**
     * @see #blkioDeviceReadBps
     */
    @CheckForNull
    public List<BlkioRateDevice> getBlkioDeviceReadBps() {
        return blkioDeviceReadBps;
    }

    public UpdateContainerCmd withBlkioDeviceReadBps(List<BlkioRateDevice> blkioDeviceReadBps) {
        this.blkioDeviceReadBps = blkioDeviceReadBps;
        return this;
    }

    /**
     * @see #blkioDeviceWriteBps
     */
    @CheckForNull
    public List<BlkioRateDevice> getBlkioDeviceWriteBps() {
        return blkioDeviceWriteBps;
    }

    public UpdateContainerCmd withBlkioDeviceWriteBps(List<BlkioRateDevice> blkioDeviceWriteBps) {
        this.blkioDeviceWriteBps = blkioDeviceWriteBps;
        return this;
    }

    /**
     * @see #blkioDeviceReadIOps
     */
    @CheckForNull
    public List<BlkioRateDevice> getBlkioDeviceReadIOps() {
        return blkioDeviceReadIOps;
    }

    public UpdateContainerCmd withBlkioDeviceReadIOps(List<BlkioRateDevice> blkioDeviceReadIOps) {
        this.blkioDeviceReadIOps = blkioDeviceReadIOps;
        return this;
    }

    /**
     * @see #blkioDeviceWriteIOps
     */
    @CheckForNull
    public List<BlkioRateDevice> getBlkioDeviceWriteIOps() {
        return blkioDeviceWriteIOps;
    }

    @Override
    public UpdateContainerCmd withBlkioDeviceWriteIOps(List<BlkioRateDevice> blkioDeviceWriteIOps) {
        this.blkioDeviceWriteIOps = blkioDeviceWriteIOps;
        return this;
    }

    /**
     * @see #cpuPeriod
     */
    @CheckForNull
    public Long getCpuPeriod() {
        return cpuPeriod;
    }

    /**
     * @see #cpuPeriod
     */
    public UpdateContainerCmd withCpuPeriod(Long cpuPeriod) {
        this.cpuPeriod = cpuPeriod;
        return this;
    }

    /**
     * @see #cpuQuota
     */
    @CheckForNull
    public Long getCpuQuota() {
        return cpuQuota;
    }

    /**
     * @see #cpuQuota
     */
    public UpdateContainerCmd withCpuQuota(Long cpuQuota) {
        this.cpuQuota = cpuQuota;
        return this;
    }

    /**
     * @see #cpusetCpus
     */
    @CheckForNull
    public String getCpusetCpus() {
        return cpusetCpus;
    }

    /**
     * @see #cpusetCpus
     */
    public UpdateContainerCmd withCpusetCpus(String cpusetCpus) {
        this.cpusetCpus = cpusetCpus;
        return this;
    }

    /**
     * @see #cpusetMems
     */
    @CheckForNull
    public String getCpusetMems() {
        return cpusetMems;
    }

    /**
     * @see #cpusetMems
     */
    public UpdateContainerCmd withCpusetMems(String cpusetMems) {
        this.cpusetMems = cpusetMems;
        return this;
    }

    /**
     * @see #cpuShares
     */
    @CheckForNull
    public Integer getCpuShares() {
        return cpuShares;
    }

    /**
     * @see #cpuShares
     */
    public UpdateContainerCmd withCpuShares(Integer cpuShares) {
        this.cpuShares = cpuShares;
        return this;
    }

    /**
     * @see #cpuRealtimePeriod
     */
    @CheckForNull
    public Long getCpuRealtimePeriod() {
        return cpuRealtimePeriod;
    }

    public UpdateContainerCmd withCpuRealtimePeriod(Long cpuRealtimePeriod) {
        this.cpuRealtimePeriod = cpuRealtimePeriod;
        return this;
    }

    /**
     * @see #cpuRealtimeRuntime
     */
    @CheckForNull
    public Long getCpuRealtimeRuntime() {
        return cpuRealtimeRuntime;
    }

    public UpdateContainerCmd withCpuRealtimeRuntime(Long cpuRealtimeRuntime) {
        this.cpuRealtimeRuntime = cpuRealtimeRuntime;
        return this;
    }

    /**
     * @see #devices
     */
    @CheckForNull
    public List<Device> getDevices() {
        return devices;
    }

    public UpdateContainerCmd withDevices(List<Device> devices) {
        this.devices = devices;
        return this;
    }

    /**
     * @see #deviceCgroupRules
     */
    @CheckForNull
    public List<String> getDeviceCgroupRules() {
        return deviceCgroupRules;
    }

    public UpdateContainerCmd withDeviceCgroupRules(List<String> deviceCgroupRules) {
        this.deviceCgroupRules = deviceCgroupRules;
        return this;
    }

    /**
     * @see #deviceRequests
     */
    @CheckForNull
    public List<DeviceRequest> getDeviceRequests() {
        return deviceRequests;
    }

    public UpdateContainerCmd withDeviceRequests(List<DeviceRequest> deviceRequests) {
        this.deviceRequests = deviceRequests;
        return this;
    }

    /**
     * @see #kernelMemory
     */
    @CheckForNull
    public Long getKernelMemory() {
        return kernelMemory;
    }

    /**
     * @see #kernelMemory
     */
    public UpdateContainerCmd withKernelMemory(Long kernelMemory) {
        this.kernelMemory = kernelMemory;
        return this;
    }

    /**
     * @see #memory
     */
    @CheckForNull
    public Long getMemory() {
        return memory;
    }

    /**
     * @see #memory
     */
    public UpdateContainerCmd withMemory(Long memory) {
        this.memory = memory;
        return this;
    }

    /**
     * @see #memoryReservation
     */
    @CheckForNull
    public Long getMemoryReservation() {
        return memoryReservation;
    }

    /**
     * @see #memoryReservation
     */
    public UpdateContainerCmd withMemoryReservation(Long memoryReservation) {
        this.memoryReservation = memoryReservation;
        return this;
    }

    /**
     * @see #memorySwap
     */
    @CheckForNull
    public Long getMemorySwap() {
        return memorySwap;
    }

    /**
     * @see #memorySwap
     */
    public UpdateContainerCmd withMemorySwap(Long memorySwap) {
        this.memorySwap = memorySwap;
        return this;
    }

    /**
     * @see #nanoCPUs
     */
    @CheckForNull
    public Long getNanoCPUs() {
        return nanoCPUs;
    }

    public UpdateContainerCmd withNanoCPUs(Long nanoCPUs) {
        this.nanoCPUs = nanoCPUs;
        return this;
    }

    /**
     * @see #oomKillDisable
     */
    @CheckForNull
    public Boolean getOomKillDisable() {
        return oomKillDisable;
    }

    public UpdateContainerCmd withOomKillDisable(Boolean oomKillDisable) {
        this.oomKillDisable = oomKillDisable;
        return this;
    }

    /**
     * @see #init
     */
    @CheckForNull
    public Boolean getInit() {
        return init;
    }

    public UpdateContainerCmd withInit(Boolean init) {
        this.init = init;
        return this;
    }

    /**
     * @see #pidsLimit
     */
    @CheckForNull
    public Long getPidsLimit() {
        return pidsLimit;
    }

    public UpdateContainerCmd withPidsLimit(Long pidsLimit) {
        this.pidsLimit = pidsLimit;
        return this;
    }

    /**
     * @see #ulimits
     */
    @CheckForNull
    public List<Ulimit> getUlimits() {
        return ulimits;
    }

    public UpdateContainerCmd withUlimits(List<Ulimit> ulimits) {
        this.ulimits = ulimits;
        return this;
    }

    /**
     * @see #restartPolicy
     */
    @CheckForNull
    public RestartPolicy getRestartPolicy() {
        return restartPolicy;
    }

    public UpdateContainerCmd withRestartPolicy(RestartPolicy restartPolicy) {
        this.restartPolicy = restartPolicy;
        return this;
    }

    /**
     * @throws NotFoundException No such container
     */
    @Override
    public UpdateContainerResponse exec() throws NotFoundException {
        return super.exec();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
