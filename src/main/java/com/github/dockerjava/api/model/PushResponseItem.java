package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a push response stream item
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class PushResponseItem extends ResponseItem {

    private static final long serialVersionUID = 8256977108011295857L;

    /**
     * Returns whether the error field indicates an error
     *
     * @returns true: the error field indicates an error, false: the error field doesn't indicate an error
     */
    @JsonIgnore
    public Boolean isErrorIndicated() {
        if (getError() == null)
            return false;

        return true;
    }
}
