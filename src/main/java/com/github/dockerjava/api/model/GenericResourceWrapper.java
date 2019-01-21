package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_39}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResourceWrapper implements Serializable {
    public static final Long serialVersionUID = 1L;

    /**
     * @since 1.39
     */
    @JsonProperty("DiscreteResourceSpec")
    private DiscreteResourceSpec discreteResourceSpec;

    /**
     * @see #discreteResourceSpec
     */
    public DiscreteResourceSpec getDiscreteResourceSpec() {
        return discreteResourceSpec;
    }

    /**
     * @see #discreteResourceSpec
     */
    public GenericResourceWrapper withDiscreteResourceSpec(DiscreteResourceSpec discreteResourceSpec) {
        this.discreteResourceSpec = discreteResourceSpec;
        return this;
    }

    public GenericResourceWrapper withKind(String kind) {
        if (this.discreteResourceSpec == null) {
            this.discreteResourceSpec = new DiscreteResourceSpec();
        }
        discreteResourceSpec.withKind(kind);
        return this;
    }

    public GenericResourceWrapper withValue(String value) {
        if (this.discreteResourceSpec == null) {
            this.discreteResourceSpec = new DiscreteResourceSpec();
        }
        discreteResourceSpec.withValue(value);
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
