package com.github.dockerjava.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;

/**
 * 
 * @author marcus
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerTopResponse {

	@JsonProperty("Titles")
	private String[] titles;

	@JsonProperty("Processes")
	private String[][] processes;
	
	public String[] getTitles() {
		return titles;
	}
	
	public String[][] getProcesses() {
		return processes;
	}
	
	@Override
    public String toString() {
		Joiner joiner = Joiner.on("; ").skipNulls();
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for(String[] fields: processes) {
			buffer.append("[" + joiner.join(fields) + "]");
		}
		buffer.append("]");
		
        return "ContainerTopResponse{" +
                "titles=" + joiner.join(titles) +
                ", processes=" + buffer.toString() +
                '}';
    }

}
