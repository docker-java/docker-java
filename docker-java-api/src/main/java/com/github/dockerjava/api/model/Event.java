package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representation of a Docker event.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since 1.16
     */
    @JsonProperty("status")
    private String status;

    /**
     * @since 1.16
     */
    @JsonProperty("id")
    private String id;

    /**
     * @since 1.16
     */
    @JsonProperty("from")
    private String from;

    /**
     * ??
     */
    @JsonProperty("node")
    private Node node;

    /*
     * @since 1.
     */
    @JsonProperty("Type")
    private EventType type;

    /**
     * @since 1.22
     */
    @JsonProperty("Action")
    private String action;

    /**
     * @since 1.22
     */
    @JsonProperty("Actor")
    private EventActor actor;

    /**
     * @since 1.16
     */
    @JsonProperty("time")
    private Long time;

    /**
     * @since 1.21
     */
    @JsonProperty("timeNano")
    private Long timeNano;

    /**
     * Default constructor for the deserialization.
     */
    public Event() {
    }

    /**
     * Constructor.
     *
     * @param id     Container ID
     * @param status Status string. List of statuses is available in <a
     *               href="https://docs.docker.com/reference/api/docker_remote_api_v1.16/#monitor-dockers-events">Docker API v.1.16</a>
     * @param from   Image, from which the container has been created
     * @param time   Event time The time is specified in milliseconds since January 1, 1970, 00:00:00 GMT
     * @since 1.16
     */
    public Event(String status, String id, String from, Long time) {
        this.status = status;
        this.id = id;
        this.from = from;
        this.time = time;
    }

    /**
     * Status of docker image or container. List of statuses is available in <a
     * href="https://docs.docker.com/reference/api/docker_remote_api_v1.16/#monitor-dockers-events">Docker API v.1.16</a>
     *
     * @return Status string
     */
    public String getStatus() {
        return status;
    }

    /**
     * @see #status
     */
    public Event withStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * Get ID of docker container.
     *
     * @return Container ID
     */
    public String getId() {
        return id;
    }

    /**
     * @see #id
     */
    public Event withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get source image of the container.
     *
     * @return Name of the parent container
     */
    public String getFrom() {
        return from;
    }

    /**
     * @see #from
     */
    public Event withFrom(String from) {
        this.from = from;
        return this;
    }

    /**
     * Get the event time. The time is specified in milliseconds since January 1, 1970, 00:00:00 GMT
     *
     * @return Event time in the specified format.
     */
    public Long getTime() {
        return time;
    }

    /**
     * @see #time
     */
    public Event withTime(Long time) {
        this.time = time;
        return this;
    }

    /**
     * @see #timeNano
     */
    @CheckForNull
    public Long getTimeNano() {
        return timeNano;
    }

    /**
     * @see #timeNano
     */
    public Event withTimenano(Long timenano) {
        this.timeNano = timenano;
        return this;
    }

    /**
     * Returns the node when working against docker swarm
     */
    public Node getNode() {
        return node;
    }

    /**
     * @see #node
     */
    public Event withNode(Node node) {
        this.node = node;
        return this;
    }

    @CheckForNull
    public EventType getType() {
        return type;
    }

    /**
     * @see #type
     */
    public Event withType(EventType type) {
        this.type = type;
        return this;
    }

    /**
     * @see #action
     */
    @CheckForNull
    public String getAction() {
        return action;
    }

    /**
     * @see #action
     */
    public Event withAction(String action) {
        this.action = action;
        return this;
    }

    /**
     * @see #actor
     */
    @CheckForNull
    public EventActor getActor() {
        return actor;
    }

    /**
     * @see #actor
     */
    public Event withEventActor(EventActor actor) {
        this.actor = actor;
        return this;
    }

    @Override
    public String toString() {
        return "Event{" +
                "status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", from='" + from + '\'' +
                ", node=" + node +
                ", type=" + type +
                ", action='" + action + '\'' +
                ", actor=" + actor +
                ", time=" + time +
                ", timeNano=" + timeNano +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(status, event.status) &&
                Objects.equals(id, event.id) &&
                Objects.equals(from, event.from) &&
                Objects.equals(node, event.node) &&
                type == event.type &&
                Objects.equals(action, event.action) &&
                Objects.equals(actor, event.actor) &&
                Objects.equals(time, event.time) &&
                Objects.equals(timeNano, event.timeNano);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, id, from, node, type, action, actor, time, timeNano);
    }
}
