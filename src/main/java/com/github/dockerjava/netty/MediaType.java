package com.github.dockerjava.netty;

public enum MediaType {

	APPLICATION_JSON("application/json"),
	APPLICATION_OCTET_STREAM("application/octet-stream"),
	APPLICATION_X_TAR("application/x-tar");

	private String mediaType;

	private MediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaType() {
		return mediaType;
	}
}
