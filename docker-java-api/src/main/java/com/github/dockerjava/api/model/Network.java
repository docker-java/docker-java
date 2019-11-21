package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@ToString
public class Network implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("Id")
    private String id;

    @FieldName("Name")
    private String name;

    @FieldName("Scope")
    private String scope;

    @FieldName("Driver")
    private String driver;

    @FieldName("EnableIPv6")
    private Boolean enableIPv6;

    @FieldName("Internal")
    private Boolean internal;

    @FieldName("IPAM")
    private Ipam ipam;

    @FieldName("Containers")
    private Map<String, ContainerNetworkConfig> containers;

    @FieldName("Options")
    private Map<String, String> options;

    @FieldName("Attachable")
    private Boolean attachable;

    @FieldName("Labels")
    public Map<String, String> labels;

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

    public Boolean getEnableIPv6() {
        return enableIPv6;
    }

    public Boolean getInternal() {
        return internal;
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

    public Boolean isAttachable() {
        return attachable;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    @EqualsAndHashCode
    @ToString
    public static class ContainerNetworkConfig {

        @FieldName("EndpointID")
        private String endpointId;

        @FieldName("MacAddress")
        private String macAddress;

        @FieldName("IPv4Address")
        private String ipv4Address;

        @FieldName("IPv6Address")
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
    }

    @EqualsAndHashCode
    @ToString
    public static class Ipam {

        @FieldName("Driver")
        private String driver;

        @FieldName("Config")
        private List<Config> config = new ArrayList<>();

        @FieldName("Options")
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

        public Ipam withDriver(String driver) {
            this.driver = driver;
            return this;
        }

        public static class Config {

            @FieldName("Subnet")
            private String subnet;

            @FieldName("IPRange")
            private String ipRange;

            @FieldName("Gateway")
            private String gateway;

            @FieldName("NetworkID")
            private String networkID;

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

            public String getNetworkID() {
                return networkID;
            }

            public void setNetworkID(String networkID) {
                this.networkID = networkID;
            }
        }
    }
}
