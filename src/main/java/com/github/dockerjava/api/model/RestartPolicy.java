package com.github.dockerjava.api.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

/**
 * Container restart policy
 * 
 *   no – Do not restart the container if it dies. (default)
 *   on-failure – Restart the container if it exits with a non-zero exit code.
 *       Can also accept an optional maximum restart count (e.g. on-failure:5).
 *   always – Always restart the container no matter what exit code is returned.
 *
 * @author marcus
 *
 */
public class RestartPolicy {

	@JsonProperty("MaximumRetryCount")
	private int maximumRetryCount = 0;

	@JsonProperty("Name")
	private String name = "";

	public RestartPolicy() {
	}

	private RestartPolicy(int maximumRetryCount, String name) {
		Preconditions.checkNotNull(name, "name is null");
		this.maximumRetryCount = maximumRetryCount;
		this.name = name;
	}
	
	public static RestartPolicy noRestart() {
		return new RestartPolicy();
	}
	
	public static RestartPolicy alwaysRestart() {
		return new RestartPolicy(0, "always");
	}
	
	public static RestartPolicy onFailureRestart(int maximumRetryCount) {
		return new RestartPolicy(maximumRetryCount, "on-failure");
	}

	public int getMaximumRetryCount() {
		return maximumRetryCount;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RestartPolicy) {
			RestartPolicy other = (RestartPolicy) obj;
			return new EqualsBuilder()
					.append(maximumRetryCount, other.getMaximumRetryCount())
					.append(name, other.getName())
					.isEquals();
		} else
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(maximumRetryCount)
				.append(name).toHashCode();
	}

}
