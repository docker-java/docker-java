package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LoadResponseItem extends ResponseItem {

    private static final long serialVersionUID = 1L;

    private static final String IMPORT_SUCCESS = "Loaded image:";

    /**
     * Returns whether the stream field indicates a successful build operation
     */
    @JsonIgnore
    public boolean isBuildSuccessIndicated() {
        if (isErrorIndicated() || getStream() == null) {
            return false;
        }

        return getStream().contains(IMPORT_SUCCESS);
    }

    @JsonIgnore
    public String getMessage() {
        if (!isBuildSuccessIndicated()) {
            return null;
        } else if (getStream().contains(IMPORT_SUCCESS)) {
            return getStream();
        }

        return null;
    }
}
