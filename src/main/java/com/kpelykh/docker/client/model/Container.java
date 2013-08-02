package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class Container {

    @JsonProperty("Id")
    public String id;

    @JsonProperty("Command")
    public String command;

    @JsonProperty("Image")
    public String image;

    @JsonProperty("Created")
    public long created;

    @JsonProperty("Status")
    public String status;

    @JsonProperty("Ports")
    public String ports;   //Example value "49164->6900, 49165->7100"

    @JsonProperty("SizeRw")
    public int size;

    @JsonProperty("SizeRootFs")
    public int sizeRootFs;

    @Override
    public String toString() {
        return "Container{" +
                "id='" + id + '\'' +
                ", command='" + command + '\'' +
                ", image='" + image + '\'' +
                ", created=" + created +
                ", status='" + status + '\'' +
                ", ports=" + ports +
                ", size=" + size +
                ", sizeRootFs=" + sizeRootFs +
                '}';
    }
}
