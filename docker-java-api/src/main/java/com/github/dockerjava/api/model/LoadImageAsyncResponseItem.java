package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author luwt
 * @date 2021/1/7.
 */
public class LoadImageAsyncResponseItem extends ResponseItem {

    private static final long serialVersionUID = 6531167952240938125L;

    private static final String LOADED_SUCCESS = "Loaded image";

    private static final String LOADED_ID_SUCCESS = "Loaded image ID: sha256:";

    private static final String LOADED_TAG_SUCCESS = "Loaded image:";

    /**
     * Returns whether the stream field indicates a successful load operation
     */
    @JsonIgnore
    public boolean isLoadSuccessIndicated() {
        if (isErrorIndicated() || getStream() == null) {
            return false;
        }
        return getStream().contains(LOADED_SUCCESS);
    }

    @JsonIgnore
    public String getImageId() {
        if (!isLoadSuccessIndicated()) {
            return null;
        }
        // loaded image successful then get image id
        if (getStream().startsWith(LOADED_ID_SUCCESS)) {
            return getStream().replaceFirst(LOADED_ID_SUCCESS, "").trim();
        }
        // else get image tag
        return getStream().replaceFirst(LOADED_TAG_SUCCESS, "").trim();
    }
}
