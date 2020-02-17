package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
public class SwarmRaftConfig implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("LogEntriesForSlowFollowers")
    private long logEntriesForSlowFollowers;

    /**
     * @since 1.24
     */
    @JsonProperty("HeartbeatTick")
    private int heartbeatTick;

    /**
     * @since 1.24
     */
    @JsonProperty("SnapshotInterval")
    private long snapshotInterval;

    /**
     * @since 1.24
     */
    @JsonProperty("ElectionTick")
    private int electionTick;

    /**
     * @see #logEntriesForSlowFollowers
     */
    @CheckForNull
    public long getLogEntriesForSlowFollowers() {
        return logEntriesForSlowFollowers;
    }

    /**
     * @see #logEntriesForSlowFollowers
     */
    public SwarmRaftConfig withLogEntriesForSlowFollowers(long logEntriesForSlowFollowers) {
        this.logEntriesForSlowFollowers = logEntriesForSlowFollowers;
        return this;
    }

    /**
     * @see #heartbeatTick
     */
    @CheckForNull
    public int getHeartbeatTick() {
        return heartbeatTick;
    }

    /**
     * @see #heartbeatTick
     */
    public SwarmRaftConfig withHeartbeatTick(int heartbeatTick) {
        this.heartbeatTick = heartbeatTick;
        return this;
    }

    /**
     * @see #snapshotInterval
     */
    @CheckForNull
    public long getSnapshotInterval() {
        return snapshotInterval;
    }

    /**
     * @see #snapshotInterval
     */
    public SwarmRaftConfig withSnapshotInterval(long snapshotInterval) {
        this.snapshotInterval = snapshotInterval;
        return this;
    }

    /**
     * @see #electionTick
     */
    @CheckForNull
    public int getElectionTick() {
        return electionTick;
    }

    /**
     * @see #electionTick
     */
    public SwarmRaftConfig withElectionTick(int electionTick) {
        this.electionTick = electionTick;
        return this;
    }
}
