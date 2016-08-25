package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.util.Date;

/**
 * @since 1.24
 */
public class Swarm extends ClusterInfo {
    public static final Long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("JoinTokens")
    private SwarmJoinTokens joinTokens;

    /**
     * @see #joinTokens
     */
    @CheckForNull
    public SwarmJoinTokens getJoinTokens() {
        return joinTokens;
    }

    /**
     * @see #joinTokens
     */
    public Swarm withJoinTokens(SwarmJoinTokens joinTokens) {
        this.joinTokens = joinTokens;
        return this;
    }

    @Override
    public Swarm withCreatedAt(Date createdAt) {
        super.withCreatedAt(createdAt);
        return this;
    }

    @Override
    public Swarm withId(String id) {
        super.withId(id);
        return this;
    }

    @Override
    public Swarm withSpec(SwarmSpec spec) {
        super.withSpec(spec);
        return this;
    }

    @Override
    public Swarm withUpdatedAt(Date updatedAt) {
        super.withUpdatedAt(updatedAt);
        return this;
    }

    @Override
    public Swarm withVersion(ResourceVersion version) {
        super.withVersion(version);
        return this;
    }
}
