package com.github.dockerjava.api.command;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Marcus Linke
 *
 */
@EqualsAndHashCode
@ToString
public class TopContainerResponse {

    @FieldName("Titles")
    private String[] titles;

    @FieldName("Processes")
    private String[][] processes;

    public String[] getTitles() {
        return titles;
    }

    public String[][] getProcesses() {
        return processes;
    }
}
