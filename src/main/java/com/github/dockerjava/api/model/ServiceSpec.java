package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * for Service
 */
@JsonIgnoreProperties(ignoreUnknown = true) // TODO remove this when all fields are implemented?
public class ServiceSpec {

	@JsonProperty("Name")
	private String name;

	@JsonProperty("TaskTemplate")
	private TaskTemplate taskTemplate;

	// TODO Mode, UpdateConfig, EndpointSpec

	public String getName() {
		return name;
	}

	public TaskTemplate getTaskTemplate() {
		return taskTemplate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}
