package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Network implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Scope")
    private String scope;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("IPAM")
    private Ipam ipam;

    @JsonProperty("Containers")
    private Map<String, ContainerNetworkConfig> containers;

    @JsonProperty("Options")
    private Map<String, String> options;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScope() {
        return scope;
    }

    public String getDriver() {
        return driver;
    }

    public Ipam getIpam() {
        return ipam;
    }

    public Map<String, ContainerNetworkConfig> getContainers() {
        return containers;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContainerNetworkConfig {

        @JsonProperty("EndpointID")
        private String endpointId;

        @JsonProperty("MacAddress")
        private String macAddress;

        @JsonProperty("IPv4Address")
        private String ipv4Address;

        @JsonProperty("IPv6Address")
        private String ipv6Address;

        public String getEndpointId() {
            return endpointId;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public String getIpv4Address() {
            return ipv4Address;
        }

        public String getIpv6Address() {
            return ipv6Address;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ipam {

        @JsonProperty("Driver")
        private String driver;

        @JsonProperty("Config")
        private List<Config> config = new ArrayList<>();

        @JsonProperty("Options")
        private Map<String, String> options = null;

        public String getDriver() {
            return driver;
        }

        public Map<String, String> getOptions() {
            return options;
        }

        public List<Config> getConfig() {
            return config;
        }

        public Ipam withConfig(List<Config> ipamConfigs) {
            config = ipamConfigs;
            return this;
        }

        public Ipam withConfig(Config... ipamConfigs) {
            config = Arrays.asList(ipamConfigs);
            return this;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Config {

            @JsonProperty("Subnet")
            private String subnet;

            @JsonProperty("IPRange")
            private String ipRange;

            @JsonProperty("Gateway")
            private String gateway;

            public String getSubnet() {
                return subnet;
            }

            public String getIpRange() {
                return ipRange;
            }

            public String getGateway() {
                return gateway;
            }

            public Config withSubnet(String subnet) {
                this.subnet = subnet;
                return this;
            }

            public Config withIpRange(String ipRange) {
                this.ipRange = ipRange;
                return this;
            }

            public Config withGateway(String gateway) {
                this.gateway = gateway;
                return this;
            }
        }
    }
}
