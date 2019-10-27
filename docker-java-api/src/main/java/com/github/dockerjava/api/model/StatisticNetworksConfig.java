package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticNetworksConfig implements Serializable {
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
     * @see #rxDropped
     */
    @CheckForNull
    public Long getRxDropped() {
        return rxDropped;
    }

    /**
     * @see #rxErrors
     */
    @CheckForNull
    public Long getRxErrors() {
        return rxErrors;
    }

    /**
     * @see #rxPackets
     */
    @CheckForNull
    public Long getRxPackets() {
        return rxPackets;
    }

    /**
     * @see #txBytes
     */
    @CheckForNull
    public Long getTxBytes() {
        return txBytes;
    }

    /**
     * @see #txDropped
     */
    @CheckForNull
    public Long getTxDropped() {
        return txDropped;
    }

    /**
     * @see #txErrors
     */
    @CheckForNull
    public Long getTxErrors() {
        return txErrors;
    }

    /**
     * @see #txPackets
     */
    @CheckForNull
    public Long getTxPackets() {
        return txPackets;
    }

    @Override
    public String toString() {
        return "StatisticNetworksConfig{" +
                "rxBytes=" + rxBytes +
                ", rxDropped=" + rxDropped +
                ", rxErrors=" + rxErrors +
                ", rxPackets=" + rxPackets +
                ", txBytes=" + txBytes +
                ", txDropped=" + txDropped +
                ", txErrors=" + txErrors +
                ", txPackets=" + txPackets +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticNetworksConfig that = (StatisticNetworksConfig) o;
        return Objects.equals(rxBytes, that.rxBytes) &&
                Objects.equals(rxDropped, that.rxDropped) &&
                Objects.equals(rxErrors, that.rxErrors) &&
                Objects.equals(rxPackets, that.rxPackets) &&
                Objects.equals(txBytes, that.txBytes) &&
                Objects.equals(txDropped, that.txDropped) &&
                Objects.equals(txErrors, that.txErrors) &&
                Objects.equals(txPackets, that.txPackets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rxBytes, rxDropped, rxErrors, rxPackets, txBytes, txDropped, txErrors, txPackets);
    }
}
