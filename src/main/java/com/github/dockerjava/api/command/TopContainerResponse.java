package com.github.dockerjava.api.command;

import static com.github.dockerjava.core.util.guava.Guava.join;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author marcus
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopContainerResponse {

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
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for(String[] fields: processes) {
			buffer.append("[" + join(fields, "; ", true) + "]");
		}
		buffer.append("]");

        return "TopContainerResponse{" +
                "titles=" + join(titles, "; ", true) +
                ", processes=" + buffer.toString() +
                '}';
    }
}
