package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Types taken form
 * {@see https://github.com/docker/engine-api/blob/release/1.10/types/network/network.go}
 * Docker named it EndpointSettings
 *
 * @see ContainerNetworkSettings
 * @author Kanstantsin Shautsou
 */
@EqualsAndHashCode
@ToString
public class ContainerNetwork implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * FIXME verify
     */
    @FieldName("IPAMConfig")
    private Ipam ipamConfig;

    /**
     * FIXME verify
     */
    @FieldName("Links")
    private Links links;

    /**
     * Add network-scoped alias for the container
     * Type picked from `docker/vendor/src/github.com/docker/engine-api/types/network/network.go`
     *
     * @since {@link RemoteApiVersion#VERSION_1_22}
     */
    @FieldName("Aliases")
    private List<String> aliases;

    @FieldName("NetworkID")
    private String networkID;

    @FieldName("EndpointID")
    private String endpointId;

    @FieldName("Gateway")
    private String gateway;

    @FieldName("IPAddress")
    private String ipAddress;

    @FieldName("IPPrefixLen")
    private Integer ipPrefixLen;

    @FieldName("IPv6Gateway")
    private String ipV6Gateway;

    @FieldName("GlobalIPv6Address")
    private String globalIPv6Address;

    @FieldName("GlobalIPv6PrefixLen")
    private Integer globalIPv6PrefixLen;

    @FieldName("MacAddress")
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

    /**
     * Docker named it EndpointIPAMConfig
     */
    public static class Ipam {

        @FieldName("IPv4Address")
        private String ipv4Address;

        @FieldName("IPv6Address")
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
