package com.github.dockerjava.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {

	@JsonProperty("Containers")
	private int containers;

	@JsonProperty("Debug")
	private boolean debug;

	@JsonProperty("Driver")
	private String driver;

	@JsonProperty("DriverStatus")
	private List<Object> driverStatuses;

	@JsonProperty("ExecutionDriver")
	private String executionDriver;

	@JsonProperty("IPv4Forwarding")
	private String IPv4Forwarding;

	@JsonProperty("Images")
	private int images;

	@JsonProperty("IndexServerAddress")
	private String IndexServerAddress;

	@JsonProperty("InitPath")
	private String initPath;

	@JsonProperty("InitSha1")
	private String initSha1;

	@JsonProperty("KernelVersion")
	private String kernelVersion;

	@JsonProperty("MemoryLimit")
	private boolean memoryLimit;

	@JsonProperty("NEventsListener")
	private long nEventListener;

	@JsonProperty("NFd")
	private int NFd;

	@JsonProperty("NGoroutines")
	private int NGoroutines;

	@JsonProperty("Sockets")
	private String[] sockets;

	@JsonProperty("SwapLimit")
	private int swapLimit;

	public boolean isDebug() {
		return debug;
	}

	public int getContainers() {
		return containers;
	}

	public String getDriver() {
		return driver;
	}

	public List<Object> getDriverStatuses() {
		return driverStatuses;
	}

	public int getImages() {
		return images;
	}

	public String getIPv4Forwarding() {
		return IPv4Forwarding;
	}

	public String getIndexServerAddress() {
		return IndexServerAddress;
	}

	public String getInitPath() {
		return initPath;
	}

	public String getInitSha1() {
		return initSha1;
	}

	public String getKernelVersion() {
		return kernelVersion;
	}

	public String[] getSockets() {
		return sockets;
	}

	public boolean isMemoryLimit() {
		return memoryLimit;
	}

	public long getnEventListener() {
		return nEventListener;
	}

	public int getNFd() {
		return NFd;
	}

	public int getNGoroutines() {
		return NGoroutines;
	}

	public int getSwapLimit() {
		return swapLimit;
	}

	public String getExecutionDriver() {
		return executionDriver;
	}

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
