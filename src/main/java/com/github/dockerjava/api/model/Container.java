package com.github.dockerjava.api.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Container {
    @JsonProperty("Command")
    private String command;

    @JsonProperty("Created")
    private Long created;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Image")
    private String image;

    @JsonProperty("Names")
    private String[] names;

    @JsonProperty("Ports")
    public Port[] ports;

    @JsonProperty("Labels")
    public Map<String, String> labels;

    @JsonProperty("Status")
    private String status;

    @JsonIgnore
    private transient String name;

    @JsonIgnore
    private transient String host;

    public String getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public String getImage() {
        return image;
    }

    public Long getCreated() {
        return created;
    }

    public String getStatus() {
        return status;
    }

    public Port[] getPorts() {
        return ports;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public String[] getNames() {
        return names;
    }

    /**
     * example for a container name in swarm could be "swarm-host-01/loving_swanson"
     * we want only "loving_swanson"
     * the result is cached in this.name and this.host
     * @return String
     */
    @JsonIgnore
    public String getNameInSwarm() {
        if (name == null) {
            String[] split = findCorrectNameInSwarm().split("/");
            if (split.length == 3) {
                host = split[1];
                name = split[2];
            } else {
                name = split[1];
            }
        }
        return name;
    }

    /**
     * see getNameInSwarm, is splits host from containername
     * @return String|null
     */
    @JsonIgnore
    public String getSwarmHost() {
        if (name == null) {
            //used to split host from name and vice versa + cache the result
            getNameInSwarm();
        }
        return host;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
            return ToStringBuilder.reflectionToString(this);
        }
    }

    @JsonIgnore
    private String findCorrectNameInSwarm() {

        //if a container is linked to another, it has multiple names
        //for example: "swarm-node-01/auth/etcd" and "swarm-node-01/etcd"
        //we search for that one, that only has one / in it, thats the "root" container
        //in our example => "etcd"
        //all other names are the alias-names in the linked containers

        //no other containers are linked to us, so just return the first one
        if (getNames().length == 1) {
            return getNames()[0];
        }

        for(String name: getNames()) {
            if (name.split("/").length <= 3) {
                return name;
            }
        }

        //fallback
        return getNames()[0];
    }
}
