package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Used in {@link Statistics}
 *
 * @author Yuting Liu
 */
@EqualsAndHashCode
@ToString
public class StatisticNetworksConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("rx_bytes")
    private Long rxBytes;

    @FieldName("rx_dropped")
    private Long rxDropped;

    @FieldName("rx_errors")
    private Long rxErrors;

    @FieldName("rx_packets")
    private Long rxPackets;

    @FieldName("tx_bytes")
    private Long txBytes;

    @FieldName("tx_dropped")
    private Long txDropped;

    @FieldName("tx_errors")
    private Long txErrors;

    @FieldName("tx_packets")
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
}
