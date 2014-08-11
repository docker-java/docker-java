package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Arrays;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Container {

	@JsonProperty("Command")
	private String command;

	@JsonProperty("Created")
	private long created;

	@JsonProperty("Id")
    private String id;

    @JsonProperty("Image")
    private String image;

    @JsonProperty("Names")
    private String[] names;

    @JsonProperty("Ports")
    public Port[] ports;

    @JsonProperty("Status")
    private String status;

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

    public String[] getNames() {
        return names;
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
}
