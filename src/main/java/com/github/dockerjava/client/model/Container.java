package com.github.dockerjava.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("Ports")    
    public Port[] ports;

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

    public Port[] getPorts() {
        return ports;
    }

    public void setPorts(Port[] ports) {
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
                ", ports=" + Arrays.toString(ports) +
                ", size=" + size +
                ", sizeRootFs=" + sizeRootFs +
                ", names=" + Arrays.toString(names) +
                '}';
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Port {

        @JsonProperty("IP")
        private String ip;

        @JsonProperty("PrivatePort")
        private Integer privatePort;

        @JsonProperty("PublicPort")
        private Integer publicPort;
        
        @JsonProperty("Type")
        private String type;
        
        public String getIp() {
			return ip;
		}
        
        public Integer getPrivatePort() {
			return privatePort;
		}
        
        public Integer getPublicPort() {
			return publicPort;
		}
        
        public String getType() {
			return type;
		}
        
        @Override
        public String toString() {
            return "Port{" +
                    "IP='" + ip + '\'' +
                    ", privatePort='" + privatePort + '\'' +
                    ", publicPort='" + publicPort + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}
