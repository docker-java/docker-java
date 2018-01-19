package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;

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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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
