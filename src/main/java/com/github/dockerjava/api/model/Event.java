package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Representation of a Docker event.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private String status;

    private String id;

    private String from;

    private long time;

    @JsonIgnoreProperties
    private Node node;

    /**
     * Default constructor for the deserialization.
     */
    public Event() {
    }

    /**
     * Constructor.
     * @param id Container ID
     * @param status Status string. 
     *        List of statuses is available in <a href="https://docs.docker.com/reference/api/docker_remote_api_v1.16/#monitor-dockers-events">Docker API v.1.16</a>
     * @param from Image, from which the container has been created
     * @param time Event time
     *      The time is specified in milliseconds since January 1, 1970, 00:00:00 GMT
     * @since TODO
     */
    public Event(String status, String id, String from, long time) {
        this.status = status;
        this.id = id;
        this.from = from;
        this.time = time;
    }
    
    /**
     * Status of docker image or container. 
     * List of statuses is available in <a href="https://docs.docker.com/reference/api/docker_remote_api_v1.16/#monitor-dockers-events">Docker API v.1.16</a>
     * @return Status string
     */
    public String getStatus() {
        return status;
    }

    /**
     * Get ID of docker container.
     * @return Container ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get source image of the container.
     * @return Name of the parent container
     */
    public String getFrom() {
        return from;
    }

    /**
     * Get the event time.
     * The time is specified in milliseconds since January 1, 1970, 00:00:00 GMT
     * @return Event time in the specified format.
     */
    public long getTime() {
        return time;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
