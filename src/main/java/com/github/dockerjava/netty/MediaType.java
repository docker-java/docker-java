package com.github.dockerjava.netty;

/**
 * This class is basically a replacement of javax.ws.rs.core.MediaType to allow simpler migration of JAX-RS code to a netty based
 * implementation.
 *
 * @author Marcus Linke
 */
public enum MediaType {

    APPLICATION_JSON("application/json"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_X_TAR("application/x-tar");

    private String mediaType;

    MediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }
}
