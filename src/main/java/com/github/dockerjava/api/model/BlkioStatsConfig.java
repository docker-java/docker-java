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
    private List<IoRecursiveConfig> ioServiceBytesRecursive;

    @JsonProperty("io_serviced_recursive")
    private List<IoRecursiveConfig> ioServicedRecursive;

    @JsonProperty("io_queue_recursive")
    private List<IoRecursiveConfig> ioQueueRecursive;

    @JsonProperty("io_service_time_recursive")
    private List<IoRecursiveConfig> ioServiceTimeRecursive;

    @JsonProperty("io_wait_time_recursive")
    private List<IoRecursiveConfig> ioWaitTimeRecursive;

    @JsonProperty("io_merged_recursive")
    private List<IoRecursiveConfig> ioMergedRecursive;

    @JsonProperty("io_time_recursive")
    private List<IoRecursiveConfig> ioTimeRecursive;

    @JsonProperty("sectors_recursive")
    private List<IoRecursiveConfig> sectorsRecursive;

    /**
     * @see #ioServiceBytesRecursive
     */
    @CheckForNull
    public List<IoRecursiveConfig> getIoServiceBytesRecursive() {
        return ioServiceBytesRecursive;
    }

    /**
     * @see #ioServicedRecursive
     */
    @CheckForNull
    public List<IoRecursiveConfig> getIoServicedRecursive() {
        return ioServicedRecursive;
    }

    /**
     * @see #ioQueueRecursive
     */
    @CheckForNull
    public List<IoRecursiveConfig> getIoQueueRecursive() {
        return ioQueueRecursive;
    }

    /**
     * @see #ioServiceTimeRecursive
     */
    @CheckForNull
    public List<IoRecursiveConfig> getIoServiceTimeRecursive() {
        return ioServiceTimeRecursive;
    }

    /**
     * @see #ioWaitTimeRecursive
     */
    @CheckForNull
    public List<IoRecursiveConfig> getIoWaitTimeRecursive() {
        return ioWaitTimeRecursive;
    }

    /**
     * @see #ioMergedRecursive
     */
    @CheckForNull
    public List<IoRecursiveConfig> getIoMergedRecursive() {
        return ioMergedRecursive;
    }

    /**
     * @see #ioTimeRecursive
     */
    @CheckForNull
    public List<IoRecursiveConfig> getIoTimeRecursive() {
        return ioTimeRecursive;
    }

    /**
     * @see #sectorsRecursive
     */
    @CheckForNull
    public List<IoRecursiveConfig> getSectorsRecursive() {
        return sectorsRecursive;
    }
}
