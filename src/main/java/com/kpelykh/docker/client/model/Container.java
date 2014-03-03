package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Arrays;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
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

    /* Example:
    "Ports": {
        "22/tcp": [
            {
                "HostIp": "0.0.0.0",
                "HostPort": "8022"
            }
        ]
    }
    */

    @JsonProperty("Ports")    
    public Ports ports;

    @JsonProperty("SizeRw")
    private int size;

    @JsonProperty("SizeRootFs")
    private int sizeRootFs;

    @JsonProperty("Names")
    private String[] names;

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

    public Ports getPorts() {
        return ports;
    }

    public void setPorts(Ports ports) {
        this.ports = ports;
    }

    public int getSize() {
        return size;
    }

    public int getSizeRootFs() {
        return sizeRootFs;
    }

    public String[] getNames() {
        return names;
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
                ", names=" + Arrays.toString(names) +
                '}';
    }
}
