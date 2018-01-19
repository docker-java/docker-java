package com.github.dockerjava.netty;

/**
 * This class is basically a replacement of javax.ws.rs.core.MediaType to allow simpler migration of JAX-RS code to a netty based
 * implementation.
 *
 * @author Marcus Linke
 * @deprecated use {@link com.github.dockerjava.core.MediaType}
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

    public com.github.dockerjava.core.MediaType toCoreMediaType() {
        return com.github.dockerjava.core.MediaType.valueOf(this.name());
    }
}
