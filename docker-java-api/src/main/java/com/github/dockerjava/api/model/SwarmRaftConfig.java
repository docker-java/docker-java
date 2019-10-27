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

    @Override
    public String toString() {
        return "SwarmRaftConfig{" +
                "logEntriesForSlowFollowers=" + logEntriesForSlowFollowers +
                ", heartbeatTick=" + heartbeatTick +
                ", snapshotInterval=" + snapshotInterval +
                ", electionTick=" + electionTick +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwarmRaftConfig that = (SwarmRaftConfig) o;
        return logEntriesForSlowFollowers == that.logEntriesForSlowFollowers &&
                heartbeatTick == that.heartbeatTick &&
                snapshotInterval == that.snapshotInterval &&
                electionTick == that.electionTick;
    }

    @Override
    public int hashCode() {
        return Objects.hash(logEntriesForSlowFollowers, heartbeatTick, snapshotInterval, electionTick);
    }
}
