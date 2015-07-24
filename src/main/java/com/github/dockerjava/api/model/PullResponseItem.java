package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a pull response stream item
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public class PullResponseItem extends ResponseItem {

    private static final long serialVersionUID = 6316219017613249047L;

    /**
     * Returns whether the status indicates a successful pull operation
     *
     * @returns true: status indicates that pull was successful, false: status doesn't indicate a successful pull
     */
    @JsonIgnore
    public boolean isPullSuccessIndicated() {
        if (getStatus() == null)
            return false;

        return (getStatus().contains("Download complete") || getStatus().contains("Image is up to date") || getStatus()
                .contains("Downloaded newer image"));
    }

}
