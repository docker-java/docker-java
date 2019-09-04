package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscreteResourceSpec extends GenericResource<String> implements Serializable {
    private static final long serialVersionUID = 1L;
}
