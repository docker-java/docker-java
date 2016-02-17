package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;

/**
 * FIXME test case
 * TODO add command
 * @author Kanstantsin Shautsou
 * @see <a href="https://docs.docker.com/engine/reference/api/docker_remote_api_v1.22/">
 *          https://docs.docker.com/engine/reference/api/docker_remote_api_v1.22/</a>
 * @since {@link RemoteApiVersion#VERSION_1_22}
 */
public class Update {
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
    private Integer memory;

    @JsonProperty("MemorySwap")
    private Integer memorySwap;

    @JsonProperty("MemoryReservation")
    private Integer memoryReservation;

    @JsonProperty("KernelMemory")
    private Integer kernelMemory;

    @CheckForNull
    public Integer getBlkioWeight() {
        return blkioWeight;
    }

    public Update setBlkioWeight(Integer blkioWeight) {
        this.blkioWeight = blkioWeight;
        return this;
    }

    @CheckForNull
    public Integer getCpuPeriod() {
        return cpuPeriod;
    }

    public Update setCpuPeriod(Integer cpuPeriod) {
        this.cpuPeriod = cpuPeriod;
        return this;
    }

    @CheckForNull
    public Integer getCpuQuota() {
        return cpuQuota;
    }

    public Update setCpuQuota(Integer cpuQuota) {
        this.cpuQuota = cpuQuota;
        return this;
    }

    @CheckForNull
    public String getCpusetCpus() {
        return cpusetCpus;
    }

    public Update setCpusetCpus(String cpusetCpus) {
        this.cpusetCpus = cpusetCpus;
        return this;
    }

    @CheckForNull
    public String getCpusetMems() {
        return cpusetMems;
    }

    public Update setCpusetMems(String cpusetMems) {
        this.cpusetMems = cpusetMems;
        return this;
    }

    @CheckForNull
    public Integer getCpuShares() {
        return cpuShares;
    }

    public Update setCpuShares(Integer cpuShares) {
        this.cpuShares = cpuShares;
        return this;
    }

    @CheckForNull
    public Integer getKernelMemory() {
        return kernelMemory;
    }

    public Update setKernelMemory(Integer kernelMemory) {
        this.kernelMemory = kernelMemory;
        return this;
    }

    @CheckForNull
    public Integer getMemory() {
        return memory;
    }

    public Update setMemory(Integer memory) {
        this.memory = memory;
        return this;
    }

    @CheckForNull
    public Integer getMemoryReservation() {
        return memoryReservation;
    }

    public Update setMemoryReservation(Integer memoryReservation) {
        this.memoryReservation = memoryReservation;
        return this;
    }

    @CheckForNull
    public Integer getMemorySwap() {
        return memorySwap;
    }

    public Update setMemorySwap(Integer memorySwap) {
        this.memorySwap = memorySwap;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("blkioWeight", blkioWeight)
                .append("cpuShares", cpuShares)
                .append("cpuPeriod", cpuPeriod)
                .append("cpuQuota", cpuQuota)
                .append("cpusetCpus", cpusetCpus)
                .append("cpusetMems", cpusetMems)
                .append("memory", memory)
                .append("memorySwap", memorySwap)
                .append("memoryReservation", memoryReservation)
                .append("kernelMemory", kernelMemory)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Update update = (Update) o;

        return new EqualsBuilder()
                .append(blkioWeight, update.blkioWeight)
                .append(cpuShares, update.cpuShares)
                .append(cpuPeriod, update.cpuPeriod)
                .append(cpuQuota, update.cpuQuota)
                .append(cpusetCpus, update.cpusetCpus)
                .append(cpusetMems, update.cpusetMems)
                .append(memory, update.memory)
                .append(memorySwap, update.memorySwap)
                .append(memoryReservation, update.memoryReservation)
                .append(kernelMemory, update.kernelMemory)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(blkioWeight)
                .append(cpuShares)
                .append(cpuPeriod)
                .append(cpuQuota)
                .append(cpusetCpus)
                .append(cpusetMems)
                .append(memory)
                .append(memorySwap)
                .append(memoryReservation)
                .append(kernelMemory)
                .toHashCode();
    }
}
