package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class Mount implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Type")
    private MountType type;

    /**
     * @since 1.24
     */
    @JsonProperty("Source")
    private String source;

    /**
     * @since 1.24
     */
    @JsonProperty("Target")
    private String target;

    /**
     * @since 1.24
     */
    @JsonProperty("ReadOnly")
    private Boolean readOnly;

    /**
     * @since 1.24
     */
    @JsonProperty("BindOptions")
    private BindOptions bindOptions;

    /**
     * @since 1.24
     */
    @JsonProperty("VolumeOptions")
    private VolumeOptions volumeOptions;

    /**
     * @see #type
     */
    @CheckForNull
    public MountType getType() {
        return type;
    }

    /**
     * @see #type
     */
    public Mount withType(MountType type) {
        this.type = type;
        return this;
    }

    /**
     * @see #source
     */
    @CheckForNull
    public String getSource() {
        return source;
    }

    /**
     * @see #source
     */
    public Mount withSource(String source) {
        this.source = source;
        return this;
    }

    /**
     * @see #target
     */
    @CheckForNull
    public String getTarget() {
        return target;
    }

    /**
     * @see #target
     */
    public Mount withTarget(String target) {
        this.target = target;
        return this;
    }

    /**
     * @see #readOnly
     */
    @CheckForNull
    public Boolean getReadOnly() {
        return readOnly;
    }

    /**
     * @see #readOnly
     */
    public Mount withReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    /**
     * @see #bindOptions
     */
    @CheckForNull
    public BindOptions getBindOptions() {
        return bindOptions;
    }

    /**
     * @see #bindOptions
     */
    public Mount withBindOptions(BindOptions bindOptions) {
        this.bindOptions = bindOptions;
        return this;
    }

    /**
     * @see #volumeOptions
     */
    @CheckForNull
    public VolumeOptions getVolumeOptions() {
        return volumeOptions;
    }

    /**
     * @see #volumeOptions
     */
    public Mount withVolumeOptions(VolumeOptions volumeOptions) {
        this.volumeOptions = volumeOptions;
        return this;
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
