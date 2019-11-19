package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

/**
 * @author Vangie Du (duwan@live.com)
 */
@EqualsAndHashCode
@ToString
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
        requireNonNull(name, "Name is null");
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
}
