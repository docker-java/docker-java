package com.github.dockerjava.api.model;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Vangie Du (duwan@live.com)
 */
public class Ulimit implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Soft")
    private Integer soft;

    @JsonProperty("Hard")
    private Integer hard;

    public Ulimit() {

    }

    public Ulimit(String name, int soft, int hard) {
        checkNotNull(name, "Name is null");

        this.name = name;
        this.soft = soft;
        this.hard = hard;
    }

    public String getName() {
        return name;
    }

    public Integer getSoft() {
        return soft;
    }

    public Integer getHard() {
        return hard;
    }

    // CHECKSTYLE:OFF
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ulimit) {
            Ulimit other = (Ulimit) obj;
            return new EqualsBuilder().append(name, other.getName()).append(soft, other.getSoft())
                    .append(hard, other.getHard()).isEquals();
        } else
            return super.equals(obj);

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(soft).append(hard).toHashCode();
    }
    // CHECKSTYLE:ON
}
