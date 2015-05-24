package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Heng WU(wuheng09@otcaix.iscas.ac.cn)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stats {

	@JsonProperty("read")
	private String read;
	
	@JsonProperty("network")
	private Object network;
	
	@JsonProperty("cpu_stats")
	private Object cpu_stats;
	
	@JsonProperty("memory_stats")
	private Object memory_stats;

	public String getRead() {
		return read;
	}

	public Object getNetwork() {
		return network;
	}

	public Object getCpu_stats() {
		return cpu_stats;
	}

	public Object getMemory_stats() {
		return memory_stats;
	}
}
