package com.github.dockerjava.core.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.UpdateContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.UpdateContainerResponse;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

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

    @JsonProperty("CpuShares")
    private Integer cpuShares;

    @JsonProperty("CpuPeriod")
    private Integer cpuPeriod;

    @JsonProperty("CpuQuota")
    private Integer cpuQuota;

    @JsonProperty("CpusetCpus")
    private String cpusetCpus;

    @JsonProperty("CpusetMems")
    private String cpusetMems;

    @JsonProperty("Memory")
    private Long memory;

    @JsonProperty("MemorySwap")
    private Long memorySwap;

    @JsonProperty("MemoryReservation")
    private Long memoryReservation;

    @JsonProperty("KernelMemory")
    private Long kernelMemory;

    public UpdateContainerCmdImpl(UpdateContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
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
     * @see #cpuPeriod
     */
    @CheckForNull
    public Integer getCpuPeriod() {
        return cpuPeriod;
    }

    /**
     * @see #cpuPeriod
     */
    public UpdateContainerCmd withCpuPeriod(Integer cpuPeriod) {
        this.cpuPeriod = cpuPeriod;
        return this;
    }

    /**
     * @see #cpuQuota
     */
    @CheckForNull
    public Integer getCpuQuota() {
        return cpuQuota;
    }

    /**
     * @see #cpuQuota
     */
    public UpdateContainerCmd withCpuQuota(Integer cpuQuota) {
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
