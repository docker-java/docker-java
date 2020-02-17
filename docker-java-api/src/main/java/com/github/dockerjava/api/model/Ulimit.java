package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

/**
 * @author Vangie Du (duwan@live.com)
 */
@JsonPropertyOrder({"Name", "Soft", "Hard"})
@EqualsAndHashCode
@ToString
public class Ulimit implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Name")
    private String name;

    private Long soft;

    private Long hard;

    public Ulimit() {
    }

    @Deprecated
    public Ulimit(String name, int soft, int hard) {
        this(name, (long) soft, (long) hard);
    }

    @JsonCreator
    public Ulimit(@JsonProperty("Name") String name, @JsonProperty("Soft") long soft, @JsonProperty("Hard") long hard) {
        requireNonNull(name, "Name is null");
        this.name = name;
        this.soft = soft;
        this.hard = hard;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    @JsonIgnore
    public Integer getSoft() {
        return soft != null ? soft.intValue() : null;
    }

    @Deprecated
    @JsonIgnore
    public Integer getHard() {
        return hard != null ? hard.intValue() : null;
    }

    @JsonProperty("Soft")
    public Long getSoftLong() {
        return soft;
    }

    @JsonProperty("Hard")
    public Long getHardLong() {
        return hard;
    }
}
