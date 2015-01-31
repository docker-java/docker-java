package com.github.dockerjava.api.model;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Container restart policy
 * 
 * <dl>
 *   <dt>no</dt>
 *   <dd>Do not restart the container if it dies. (default)</dd>
 *   
 *   <dt>on-failure</dt>
 *   <dd>Restart the container if it exits with a non-zero exit code.
 *       Can also accept an optional maximum restart count (e.g. on-failure:5).<dd>
 *   
 *   <dt>always</dt>
 *   <dd>Always restart the container no matter what exit code is returned.<dd>
 * </dl>
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
		checkNotNull(name, "name is null");
		this.maximumRetryCount = maximumRetryCount;
		this.name = name;
	}

	/**
	 * Do not restart the container if it dies. (default)
	 */
	public static RestartPolicy noRestart() {
		return new RestartPolicy();
	}

	/**
	 * Always restart the container no matter what exit code is returned.
	 */
	public static RestartPolicy alwaysRestart() {
		return new RestartPolicy(0, "always");
	}

	/**
	 * Restart the container if it exits with a non-zero exit code.
	 * 
	 * @param maximumRetryCount the maximum number of restarts.
	 *        Set to <code>0</code> for unlimited retries.
	 */
	public static RestartPolicy onFailureRestart(int maximumRetryCount) {
		return new RestartPolicy(maximumRetryCount, "on-failure");
	}

	public int getMaximumRetryCount() {
		return maximumRetryCount;
	}

	public String getName() {
		return name;
	}

	/**
	 * Parses a textual restart polixy specification (as used by the Docker CLI) 
	 * to a {@link RestartPolicy}.
	 * 
	 * @param serialized the specification, e.g. <code>on-failure:2</code>
	 * @return a {@link RestartPolicy} matching the specification
	 * @throws IllegalArgumentException if the specification cannot be parsed
	 */
	public static RestartPolicy parse(String serialized) throws IllegalArgumentException {
		try {
			String[] parts = serialized.split(":");
			String name = parts[0];
			if ("no".equals(name))
				return noRestart();
			if ("always".equals(name))
				return alwaysRestart();
			if ("on-failure".equals(name)) {
				int count = 0;
				if (parts.length == 2) {
					count = Integer.parseInt(parts[1]);
				}
				return onFailureRestart(count);
			}
			throw new IllegalArgumentException();
		} catch (Exception e) {
			throw new IllegalArgumentException("Error parsing RestartPolicy '" + serialized + "'");
		}
	}

	/**
	 * Returns a string representation of this {@link RestartPolicy}.
	 * The format is <code>name[:count]</code>, like the argument in {@link #parse(String)}.
	 * 
	 * @return a string representation of this {@link RestartPolicy}
	 */
	@Override
	public String toString() {
		String result = name.isEmpty() ? "no" : name;
		return maximumRetryCount > 0 ? result + ":" + maximumRetryCount : result;
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
