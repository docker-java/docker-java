package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;

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
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (String[] fields : processes) {
            buffer.append("[" + Joiner.on("; ").skipNulls().join(fields) + "]");
        }
        buffer.append("]");

        return "TopContainerResponse{" + "titles=" + Joiner.on("; ").skipNulls().join(titles) + ", processes="
                + buffer.toString() + '}';
    }
}
