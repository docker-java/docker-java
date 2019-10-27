package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwarmOrchestration implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("TaskHistoryRetentionLimit")
    private int taskHistoryRententionLimit;

    /**
     * @see #taskHistoryRententionLimit
     */
    @CheckForNull
    public int getTaskHistoryRententionLimit() {
        return taskHistoryRententionLimit;
    }

    /**
     * @see #taskHistoryRententionLimit
     */
    public SwarmOrchestration withTaskHistoryRententionLimit(int taskHistoryRententionLimit) {
        this.taskHistoryRententionLimit = taskHistoryRententionLimit;
        return this;
    }

    @Override
    public String toString() {
        return "SwarmOrchestration{" +
                "taskHistoryRententionLimit=" + taskHistoryRententionLimit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwarmOrchestration that = (SwarmOrchestration) o;
        return taskHistoryRententionLimit == that.taskHistoryRententionLimit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskHistoryRententionLimit);
    }
}
