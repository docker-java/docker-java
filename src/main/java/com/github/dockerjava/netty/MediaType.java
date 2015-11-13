package com.github.dockerjava.netty;

public enum MediaType {

	APPLICATION_JSON("application/json");
	
	private String mediaType;
	
	private MediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	
	public String getMediaType() {
		return mediaType;
	}
}
