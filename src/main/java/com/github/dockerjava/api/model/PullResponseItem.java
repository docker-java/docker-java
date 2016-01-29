package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a pull response stream item
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullResponseItem extends ResponseItem {

    private static final long serialVersionUID = -2575482839766823293L;

    private static final String[] SUCCESS_STRINGS = new String[]{
            "this image was pulled from a legacy registry",
            "Downloaded newer image",
            "Image is up to date",
            "Download complete",
            " downloaded"                   //Swarm sends another text, when download is complete!
    };

    /**
     * Returns whether the status indicates a successful pull operation
     *
     * @returns true: status indicates that pull was successful, false: status doesn't indicate a successful pull
     */
    @JsonIgnore
    public boolean isPullSuccessIndicated() {
        if (isErrorIndicated() || getStatus() == null) {
            return false;
        }

        for(String str : SUCCESS_STRINGS) {
            if (getStatus().contains(str)) return true;
        }
        return false;
    }
}
