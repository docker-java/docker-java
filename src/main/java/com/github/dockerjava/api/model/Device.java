package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

public class Device {

	@JsonProperty("CgroupPermissions")
	private String cGroupPermissions = "";

	@JsonProperty("PathOnHost")
	private String pathOnHost = null;

	@JsonProperty("PathInContainer")
	private String pathInContainer = null;

	public Device() {
	}

	public Device(String cGroupPermissions, String pathInContainer,
			String pathOnHost) {
		Preconditions.checkNotNull(cGroupPermissions,
				"cGroupPermissions is null");
		Preconditions.checkNotNull(pathInContainer, "pathInContainer is null");
		Preconditions.checkNotNull(pathOnHost, "pathOnHost is null");
		this.cGroupPermissions = cGroupPermissions;
		this.pathInContainer = pathInContainer;
		this.pathOnHost = pathOnHost;
	}

	public String getcGroupPermissions() {
		return cGroupPermissions;
	}

	public String getPathInContainer() {
		return pathInContainer;
	}

	public String getPathOnHost() {
		return pathOnHost;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Device) {
			Device other = (Device) obj;
			return new EqualsBuilder()
					.append(cGroupPermissions, other.getcGroupPermissions())
					.append(pathInContainer, other.getPathInContainer())
					.append(pathOnHost, other.getPathOnHost()).isEquals();
		} else
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(cGroupPermissions)
				.append(pathInContainer).append(pathOnHost).toHashCode();
	}

}
