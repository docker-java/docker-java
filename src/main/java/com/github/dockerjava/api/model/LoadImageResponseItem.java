package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Kanstantsin Shautsou
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadImageResponseItem extends ResponseItem {
    private static final long serialVersionUID = 1L;

    private static final String LOAD_SUCCESS = "Loaded image:";

    @JsonIgnore
    public boolean isLoadedOK() {
        if (isErrorIndicated() || getStream() == null) {
            return false;
        }

        return getStream().contains(LOAD_SUCCESS);
    }
}
