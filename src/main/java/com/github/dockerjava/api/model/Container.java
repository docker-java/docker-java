package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Network.Ipam;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

/**
 * Used for Listing containers.
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
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

    /**
     * @since since {@link RemoteApiVersion#VERSION_1_21}
     */
    @JsonProperty("ImageID")
    private String imageId;

    @JsonProperty("Names")
    private String[] names;

    @JsonProperty("Ports")
    public Port[] ports;

    @JsonProperty("Labels")
    public Map<String, String> labels;

    @JsonProperty("Status")
    private String status;

    /**
     * @since > ~{@link RemoteApiVersion#VERSION_1_19}
     */
    @JsonProperty("SizeRw")
    private Long sizeRw;

    /**
     * Returns only when {@link ListContainersCmd#withShowSize(java.lang.Boolean)} set
     *
     * @since > ~{@link RemoteApiVersion#VERSION_1_19}
     */
    @JsonProperty("SizeRootFs")
    private Long sizeRootFs;

    @JsonProperty("HostConfig")
    private HostConfig hostConfig;

    /**
     * Docker API docs says "list of networks", but json names `networkSettings`.
     * So, reusing existed NetworkSettings model object.
     *
     * @since > ~{@link RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("NetworkSettings")
    private NetworkSettings networkSettings;

    public String getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public String getImage() {
        return image;
    }

    @CheckForNull
    public String getImageId() {
        return imageId;
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
     * @see #sizeRw
     */
    @CheckForNull
    public Long getSizeRw() {
        return sizeRw;
    }

    /**
     * @see #sizeRootFs
     */
    @CheckForNull
    public Long getSizeRootFs() {
        return sizeRootFs;
    }

    /**
     * @see #networkSettings
     */
    @CheckForNull
    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Container container = (Container) o;

        return new EqualsBuilder()
                .append(command, container.command)
                .append(created, container.created)
                .append(id, container.id)
                .append(image, container.image)
                .append(imageId, container.imageId)
                .append(names, container.names)
                .append(ports, container.ports)
                .append(labels, container.labels)
                .append(status, container.status)
                .append(sizeRw, container.sizeRw)
                .append(sizeRootFs, container.sizeRootFs)
                .append(hostConfig, container.hostConfig)
                .append(networkSettings, container.networkSettings)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(command)
                .append(created)
                .append(id)
                .append(image)
                .append(imageId)
                .append(names)
                .append(ports)
                .append(labels)
                .append(status)
                .append(sizeRw)
                .append(sizeRootFs)
                .append(hostConfig)
                .append(networkSettings)
                .toHashCode();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class HostConfig {
        @JsonProperty("NetworkMode")
        private String networkMode;

        public String getNetworkMode() {
            return networkMode;
        }
    }

    /**
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class NetworkSettings {
        @JsonProperty("Networks")
        private Map<String, Network> networks;

        public Map<String, Network> getNetworks() {
            return networks;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static final class Network {
            /**
             * FIXME verify
             */
            @JsonProperty("IPAMConfig")
            private Ipam.Config ipamConfig;

            /**
             * FIXME verify
             */
            @JsonProperty("Links")
            private List<String> links;

            /**
             * FIXME no docs, unknown field.
             * Type picked from `docker/vendor/src/github.com/docker/engine-api/types/network/network.go`
             */
            @JsonProperty("Aliases")
            private List<String> aliases;

            @JsonProperty("NetworkID")
            private String networkID;

            @JsonProperty("EndpointID")
            private String endpointId;

            @JsonProperty("Gateway")
            private String gateway;

            @JsonProperty("IPAddress")
            private String ipAddress;

            @JsonProperty("IPPrefixLen")
            private Integer ipPrefixLen;

            @JsonProperty("IPv6Gateway")
            private String ipV6Gateway;

            @JsonProperty("GlobalIPv6Address")
            private String globalIPv6Address;

            @JsonProperty("GlobalIPv6PrefixLen")
            private Integer globalIPv6PrefixLen;

            @JsonProperty("MacAddress")
            private String macAddress;

            @CheckForNull
            public List<String> getAliases() {
                return aliases;
            }

            public String getEndpointId() {
                return endpointId;
            }

            public String getGateway() {
                return gateway;
            }

            public String getGlobalIPv6Address() {
                return globalIPv6Address;
            }

            public Integer getGlobalIPv6PrefixLen() {
                return globalIPv6PrefixLen;
            }

            public String getIpAddress() {
                return ipAddress;
            }

            public Ipam.Config getIpamConfig() {
                return ipamConfig;
            }

            public Integer getIpPrefixLen() {
                return ipPrefixLen;
            }

            public String getIpV6Gateway() {
                return ipV6Gateway;
            }

            public List<String> getLinks() {
                return links;
            }

            public String getMacAddress() {
                return macAddress;
            }

            public String getNetworkID() {
                return networkID;
            }

            @Override
            public String toString() {
                return new ToStringBuilder(this)
                        .append("aliases", aliases)
                        .append("ipamConfig", ipamConfig)
                        .append("links", links)
                        .append("networkID", networkID)
                        .append("endpointId", endpointId)
                        .append("gateway", gateway)
                        .append("ipAddress", ipAddress)
                        .append("ipPrefixLen", ipPrefixLen)
                        .append("ipV6Gateway", ipV6Gateway)
                        .append("globalIPv6Address", globalIPv6Address)
                        .append("globalIPv6PrefixLen", globalIPv6PrefixLen)
                        .append("macAddress", macAddress)
                        .toString();
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;

                if (o == null || getClass() != o.getClass()) return false;

                Network network = (Network) o;

                return new EqualsBuilder()
                        .append(ipamConfig, network.ipamConfig)
                        .append(links, network.links)
                        .append(aliases, network.aliases)
                        .append(networkID, network.networkID)
                        .append(endpointId, network.endpointId)
                        .append(gateway, network.gateway)
                        .append(ipAddress, network.ipAddress)
                        .append(ipPrefixLen, network.ipPrefixLen)
                        .append(ipV6Gateway, network.ipV6Gateway)
                        .append(globalIPv6Address, network.globalIPv6Address)
                        .append(globalIPv6PrefixLen, network.globalIPv6PrefixLen)
                        .append(macAddress, network.macAddress)
                        .isEquals();
            }

            @Override
            public int hashCode() {
                return new HashCodeBuilder(17, 37)
                        .append(ipamConfig)
                        .append(links)
                        .append(aliases)
                        .append(networkID)
                        .append(endpointId)
                        .append(gateway)
                        .append(ipAddress)
                        .append(ipPrefixLen)
                        .append(ipV6Gateway)
                        .append(globalIPv6Address)
                        .append(globalIPv6PrefixLen)
                        .append(macAddress)
                        .toHashCode();
            }
        }
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Port port = (Port) o;

            return new EqualsBuilder()
                    .append(ip, port.ip)
                    .append(privatePort, port.privatePort)
                    .append(publicPort, port.publicPort)
                    .append(type, port.type)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(ip)
                    .append(privatePort)
                    .append(publicPort)
                    .append(type)
                    .toHashCode();
        }
    }
}
