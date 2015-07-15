package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a pull response stream item
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class PullResponseItem extends ResponseItem {

    private static final long serialVersionUID = 6316219017613249047L;

}
