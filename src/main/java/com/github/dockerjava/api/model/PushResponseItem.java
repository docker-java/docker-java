package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a push response stream item
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushResponseItem extends ResponseItem {

    private static final long serialVersionUID = 8256977108011295857L;

}
