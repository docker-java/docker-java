package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Vangie Du (duwan@live.com)
 */
public class Ulimit {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Soft")
    private int soft;

    @JsonProperty("Hard")
    private int hard;

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

    public int getSoft() {
        return soft;
    }

    public int getHard() {
        return hard;
    }

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
}
