package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class BlkioStatsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("io_service_bytes_recursive")
    private List<Long> ioServiceBytesRecursive;

    @JsonProperty("io_serviced_recursive")
    private List<Long> ioServicedRecursive;

    @JsonProperty("io_queue_recursive")
    private List<Long> ioQueueRecursive;

    @JsonProperty("io_service_time_recursive")
    private List<Long> ioServiceTimeRecursive;

    @JsonProperty("io_wait_time_recursive")
    private List<Long> ioWaitTimeRecursive;

    @JsonProperty("io_merged_recursive")
    private List<Long> ioMergedRecursive;

    @JsonProperty("io_time_recursive")
    private List<Long> ioTimeRecursive;

    @JsonProperty("sectors_recursive")
    private List<Long> sectorsRecursive;

    /**
     * @see #ioServiceBytesRecursive
     */
    @CheckForNull
    public List<Long> getIoServiceBytesRecursive() {
        return ioServiceBytesRecursive;
    }

    /**
     * @see #ioServicedRecursive
     */
    @CheckForNull
    public List<Long> getIoServicedRecursive() {
        return ioServicedRecursive;
    }

    /**
     * @see #ioQueueRecursive
     */
    @CheckForNull
    public List<Long> getIoQueueRecursive() {
        return ioQueueRecursive;
    }

    /**
     * @see #ioServiceTimeRecursive
     */
    @CheckForNull
    public List<Long> getIoServiceTimeRecursive() {
        return ioServiceTimeRecursive;
    }

    /**
     * @see #ioWaitTimeRecursive
     */
    @CheckForNull
    public List<Long> getIoWaitTimeRecursive() {
        return ioWaitTimeRecursive;
    }

    /**
     * @see #ioMergedRecursive
     */
    @CheckForNull
    public List<Long> getIoMergedRecursive() {
        return ioMergedRecursive;
    }

    /**
     * @see #ioTimeRecursive
     */
    @CheckForNull
    public List<Long> getIoTimeRecursive() {
        return ioTimeRecursive;
    }

    /**
     * @see #sectorsRecursive
     */
    @CheckForNull
    public List<Long> getSectorsRecursive() {
        return sectorsRecursive;
    }
}
