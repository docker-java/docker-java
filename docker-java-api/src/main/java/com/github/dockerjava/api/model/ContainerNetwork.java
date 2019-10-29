package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Types taken form
 * {@see https://github.com/docker/engine-api/blob/release/1.10/types/network/network.go}
 * Docker named it EndpointSettings
 *
 * @see ContainerNetworkSettings
 * @author Kanstantsin Shautsou
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerNetwork implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * FIXME verify
     */
    @JsonProperty("IPAMConfig")
    private Ipam ipamConfig;

    /**
     * FIXME verify
     */
    @JsonProperty("Links")
    private Links links;

    /**
     * Add network-scoped alias for the container
     * Type picked from `docker/vendor/src/github.com/docker/engine-api/types/network/network.go`
     *
     * @since {@link RemoteApiVersion#VERSION_1_22}
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

    /**
     * @see #aliases
     */
    @CheckForNull
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * @see #aliases
     */
    public ContainerNetwork withAliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    /**
     * @see #aliases
     */
    public ContainerNetwork withAliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

    /**
     * @see #endpointId
     */
    @CheckForNull
    public String getEndpointId() {
        return endpointId;
    }

    /**
     * @see #endpointId
     */
    public ContainerNetwork withEndpointId(String endpointId) {
        this.endpointId = endpointId;
        return this;
    }

    /**
     * @see #gateway
     */
    @CheckForNull
    public String getGateway() {
        return gateway;
    }

    /**
     * @see #gateway
     */
    public ContainerNetwork withGateway(String gateway) {
        this.gateway = gateway;
        return this;
    }

    /**
     * @see #globalIPv6Address
     */
    @CheckForNull
    public String getGlobalIPv6Address() {
        return globalIPv6Address;
    }

    /**
     * @see #globalIPv6Address
     */
    public ContainerNetwork withGlobalIPv6Address(String globalIPv6Address) {
        this.globalIPv6Address = globalIPv6Address;
        return this;
    }

    /**
     * @see #globalIPv6PrefixLen
     */
    @CheckForNull
    public Integer getGlobalIPv6PrefixLen() {
        return globalIPv6PrefixLen;
    }

    /**
     * @see #globalIPv6PrefixLen
     */
    public ContainerNetwork withGlobalIPv6PrefixLen(Integer globalIPv6PrefixLen) {
        this.globalIPv6PrefixLen = globalIPv6PrefixLen;
        return this;
    }

    /**
     * @see #ipAddress
     */
    @CheckForNull
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @see #ipAddress
     */
    public ContainerNetwork withIpv4Address(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    /**
     * @see #ipamConfig
     */
    @CheckForNull
    public Ipam getIpamConfig() {
        return ipamConfig;
    }

    /**
     * @see #ipamConfig
     */
    public ContainerNetwork withIpamConfig(Ipam ipamConfig) {
        this.ipamConfig = ipamConfig;
        return this;
    }

    /**
     * @see #ipPrefixLen
     */
    @CheckForNull
    public Integer getIpPrefixLen() {
        return ipPrefixLen;
    }

    /**
     * @see #ipPrefixLen
     */
    public ContainerNetwork withIpPrefixLen(Integer ipPrefixLen) {
        this.ipPrefixLen = ipPrefixLen;
        return this;
    }

    /**
     * @see #ipV6Gateway
     */
    @CheckForNull
    public String getIpV6Gateway() {
        return ipV6Gateway;
    }

    /**
     * @see #ipV6Gateway
     */
    public ContainerNetwork withIpV6Gateway(String ipV6Gateway) {
        this.ipV6Gateway = ipV6Gateway;
        return this;
    }

    /**
     * @see #links
     */
    @CheckForNull
    @JsonIgnore
    public Link[] getLinks() {
        return links == null ? new Link[0] : links.getLinks();
    }

    /**
     * @see #links
     */
    public ContainerNetwork withLinks(List<Link> links) {
        this.links = new Links(links);
        return this;
    }

    /**
     * @see #links
     */
    public ContainerNetwork withLinks(Link... links) {
        this.links = new Links(links);
        return this;
    }

    /**
     * @see #macAddress
     */
    @CheckForNull
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * @see #macAddress
     */
    public ContainerNetwork withMacAddress(String macAddress) {
        this.macAddress = macAddress;
        return this;
    }

    /**
     * @see #networkID
     */
    @CheckForNull
    public String getNetworkID() {
        return networkID;
    }

    /**
     * @see #networkID
     */
    public ContainerNetwork withNetworkID(String networkID) {
        this.networkID = networkID;
        return this;
    }

    @Override
    public String toString() {
        return "ContainerNetwork{" +
                "ipamConfig=" + ipamConfig +
                ", links=" + links +
                ", aliases=" + aliases +
                ", networkID='" + networkID + '\'' +
                ", endpointId='" + endpointId + '\'' +
                ", gateway='" + gateway + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", ipPrefixLen=" + ipPrefixLen +
                ", ipV6Gateway='" + ipV6Gateway + '\'' +
                ", globalIPv6Address='" + globalIPv6Address + '\'' +
                ", globalIPv6PrefixLen=" + globalIPv6PrefixLen +
                ", macAddress='" + macAddress + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerNetwork that = (ContainerNetwork) o;
        return Objects.equals(ipamConfig, that.ipamConfig) &&
                Objects.equals(links, that.links) &&
                Objects.equals(aliases, that.aliases) &&
                Objects.equals(networkID, that.networkID) &&
                Objects.equals(endpointId, that.endpointId) &&
                Objects.equals(gateway, that.gateway) &&
                Objects.equals(ipAddress, that.ipAddress) &&
                Objects.equals(ipPrefixLen, that.ipPrefixLen) &&
                Objects.equals(ipV6Gateway, that.ipV6Gateway) &&
                Objects.equals(globalIPv6Address, that.globalIPv6Address) &&
                Objects.equals(globalIPv6PrefixLen, that.globalIPv6PrefixLen) &&
                Objects.equals(macAddress, that.macAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                ipamConfig,
                links,
                aliases,
                networkID,
                endpointId,
                gateway,
                ipAddress,
                ipPrefixLen,
                ipV6Gateway,
                globalIPv6Address,
                globalIPv6PrefixLen,
                macAddress
        );
    }

    /**
     * Docker named it EndpointIPAMConfig
     */
    public static class Ipam {

        @JsonProperty("IPv4Address")
        private String ipv4Address;

        @JsonProperty("IPv6Address")
        private String ipv6Address;

        public String getIpv4Address() {
            return ipv4Address;
        }

        public String getIpv6Address() {
            return ipv6Address;
        }

        public Ipam withIpv4Address(String ipv4Address) {
            this.ipv4Address = ipv4Address;
            return this;
        }

        public Ipam withIpv6Address(String ipv6Address) {
            this.ipv6Address = ipv6Address;
            return this;
        }
    }
}
