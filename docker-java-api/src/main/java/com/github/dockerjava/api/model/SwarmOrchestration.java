package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class SwarmOrchestration implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @FieldName("TaskHistoryRetentionLimit")
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
}
