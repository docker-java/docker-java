package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.DockerObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Marcus Linke
 *
 */
@EqualsAndHashCode(callSuper = true)
@ToString
public class TopContainerResponse extends DockerObject {

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
}
