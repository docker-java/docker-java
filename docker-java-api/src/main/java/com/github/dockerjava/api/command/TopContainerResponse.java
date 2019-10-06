package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Marcus Linke
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
        StringBuilder buffer = new StringBuilder("TopContainerResponse{");
        buffer.append("titles=");
        buffer.append(String.join("; ", titles));
        buffer.append(", processes=");
        buffer.append("[");
        for (String[] fields : processes) {
            buffer.append("[")
                    .append(String.join("; ", fields))
                    .append("]");
        }
        buffer.append("]");
        buffer.append("}");

        return buffer.toString();
    }
}
