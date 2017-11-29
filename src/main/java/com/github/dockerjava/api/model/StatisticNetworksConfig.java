package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */

@JsonIgnoreProperties(ignoreUnknown = true)
class StatisticNetWorksConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("rx_bytes")
    private Long rxBytes;

    @JsonProperty("rx_dropped")
    private Long rxDropped;

    @JsonProperty("rx_errors")
    private Long rxErrors;

    @JsonProperty("rx_packets")
    private Long rxPackets;

    @JsonProperty("tx_bytes")
    private Long txBytes;

    @JsonProperty("tx_dropped")
    private Long txDropped;

    @JsonProperty("tx_errors")
    private Long txErrors;

    @JsonProperty("tx_packets")
    private Long txPackets;

    /**
     * @see #rxBytes
     */
    @CheckForNull
    public Long getRxBytes() {
        return rxBytes;
    }

    /**
     * @see #rxBytes
     */
    public StatisticNetWorksConfig withRxBytes(Long rxBytes) {
        this.rxBytes = rxBytes;
        return this;
    }

    /**
     * @see #rxDropped
     */
    @CheckForNull
    public Long getRxDropped() {
        return rxDropped;
    }

    /**
     * @see #rxDropped
     */
    public StatisticNetWorksConfig withRxDropped(Long rxDropped) {
        this.rxDropped = rxDropped;
        return this;
    }

    /**
     * @see #rxErrors
     */
    @CheckForNull
    public Long getRxErrors() {
        return rxErrors;
    }

    /**
     * @see #rxErrors
     */
    public StatisticNetWorksConfig withRxErrors(Long rxErrors) {
        this.rxErrors = rxErrors;
        return this;
    }

    /**
     * @see #rxPackets
     */
    @CheckForNull
    public Long getRxPackets() {
        return rxPackets;
    }

    /**
     * @see #rxPackets
     */
    public StatisticNetWorksConfig withRxPackets(Long rxPackets) {
        this.rxPackets = rxPackets;
        return this;
    }

    /**
     * @see #txBytes
     */
    @CheckForNull
    public Long getTxBytes() {
        return txBytes;
    }

    /**
     * @see #txBytes
     */
    public StatisticNetWorksConfig withTxBytes(Long txBytes) {
        this.txBytes = txBytes;
        return this;
    }

    /**
     * @see #txDropped
     */
    @CheckForNull
    public Long getTxDropped() {
        return txDropped;
    }

    /**
     * @see #txDropped
     */
    public StatisticNetWorksConfig withTxDropped(Long txDropped) {
        this.txDropped = txDropped;
        return this;
    }

    /**
     * @see #txErrors
     */
    @CheckForNull
    public Long getTxErrors() {
        return txErrors;
    }

    /**
     * @see #txErrors
     */
    public StatisticNetWorksConfig withTxErrors(Long txErrors) {
        this.txErrors = txErrors;
        return this;
    }

    /**
     * @see #txPackets
     */
    @CheckForNull
    public Long getTxPackets() {
        return txPackets;
    }

    /**
     * @see #txPackets
     */
    public StatisticNetWorksConfig withTxPackets(Long txPackets) {
        this.txPackets = txPackets;
        return this;
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
