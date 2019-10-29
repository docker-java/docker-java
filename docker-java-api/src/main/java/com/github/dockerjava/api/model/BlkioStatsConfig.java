package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlkioStatsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("io_service_bytes_recursive")
    private List<BlkioStatEntry> ioServiceBytesRecursive;

    @JsonProperty("io_serviced_recursive")
    private List<BlkioStatEntry> ioServicedRecursive;

    @JsonProperty("io_queue_recursive")
    private List<BlkioStatEntry> ioQueueRecursive;

    @JsonProperty("io_service_time_recursive")
    private List<BlkioStatEntry> ioServiceTimeRecursive;

    @JsonProperty("io_wait_time_recursive")
    private List<BlkioStatEntry> ioWaitTimeRecursive;

    @JsonProperty("io_merged_recursive")
    private List<BlkioStatEntry> ioMergedRecursive;

    @JsonProperty("io_time_recursive")
    private List<BlkioStatEntry> ioTimeRecursive;

    @JsonProperty("sectors_recursive")
    private List<BlkioStatEntry> sectorsRecursive;

    /**
     * @see #ioServiceBytesRecursive
     */
    @CheckForNull
    public List<BlkioStatEntry> getIoServiceBytesRecursive() {
        return ioServiceBytesRecursive;
    }

    /**
     * @see #ioServicedRecursive
     */
    @CheckForNull
    public List<BlkioStatEntry> getIoServicedRecursive() {
        return ioServicedRecursive;
    }

    /**
     * @see #ioQueueRecursive
     */
    @CheckForNull
    public List<BlkioStatEntry> getIoQueueRecursive() {
        return ioQueueRecursive;
    }

    /**
     * @see #ioServiceTimeRecursive
     */
    @CheckForNull
    public List<BlkioStatEntry> getIoServiceTimeRecursive() {
        return ioServiceTimeRecursive;
    }

    /**
     * @see #ioWaitTimeRecursive
     */
    @CheckForNull
    public List<BlkioStatEntry> getIoWaitTimeRecursive() {
        return ioWaitTimeRecursive;
    }

    /**
     * @see #ioMergedRecursive
     */
    @CheckForNull
    public List<BlkioStatEntry> getIoMergedRecursive() {
        return ioMergedRecursive;
    }

    /**
     * @see #ioTimeRecursive
     */
    @CheckForNull
    public List<BlkioStatEntry> getIoTimeRecursive() {
        return ioTimeRecursive;
    }

    /**
     * @see #sectorsRecursive
     */
    @CheckForNull
    public List<BlkioStatEntry> getSectorsRecursive() {
        return sectorsRecursive;
    }

    @Override
    public String toString() {
        return "BlkioStatsConfig{" +
                "ioServiceBytesRecursive=" + ioServiceBytesRecursive +
                ", ioServicedRecursive=" + ioServicedRecursive +
                ", ioQueueRecursive=" + ioQueueRecursive +
                ", ioServiceTimeRecursive=" + ioServiceTimeRecursive +
                ", ioWaitTimeRecursive=" + ioWaitTimeRecursive +
                ", ioMergedRecursive=" + ioMergedRecursive +
                ", ioTimeRecursive=" + ioTimeRecursive +
                ", sectorsRecursive=" + sectorsRecursive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlkioStatsConfig that = (BlkioStatsConfig) o;
        return Objects.equals(ioServiceBytesRecursive, that.ioServiceBytesRecursive) &&
                Objects.equals(ioServicedRecursive, that.ioServicedRecursive) &&
                Objects.equals(ioQueueRecursive, that.ioQueueRecursive) &&
                Objects.equals(ioServiceTimeRecursive, that.ioServiceTimeRecursive) &&
                Objects.equals(ioWaitTimeRecursive, that.ioWaitTimeRecursive) &&
                Objects.equals(ioMergedRecursive, that.ioMergedRecursive) &&
                Objects.equals(ioTimeRecursive, that.ioTimeRecursive) &&
                Objects.equals(sectorsRecursive, that.sectorsRecursive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                ioServiceBytesRecursive,
                ioServicedRecursive,
                ioQueueRecursive,
                ioServiceTimeRecursive,
                ioWaitTimeRecursive,
                ioMergedRecursive,
                ioTimeRecursive,
                sectorsRecursive
        );
    }
}
