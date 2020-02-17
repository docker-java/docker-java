package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
@EqualsAndHashCode
@ToString
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
}
