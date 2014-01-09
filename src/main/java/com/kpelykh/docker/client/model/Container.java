package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class Container {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Command")
    private String command;

    @JsonProperty("Image")
    private String image;

    @JsonProperty("Created")
    private long created;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Ports")
    private String ports;   //Example value "49164->6900, 49165->7100"

    @JsonProperty("SizeRw")
    private int size;

    @JsonProperty("SizeRootFs")
    private int sizeRootFs;

    public String getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public String getImage() {
        return image;
    }

    public long getCreated() {
        return created;
    }

    public String getStatus() {
        return status;
    }

    public String getPorts() {
        return ports;
    }

    public int getSize() {
        return size;
    }

    public int getSizeRootFs() {
        return sizeRootFs;
    }

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
