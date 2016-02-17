package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.util.List;

/**
 * @see ContainerNetworkSettings
 * @author Kanstantsin Shautsou
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerNetwork {
    /**
     * FIXME verify
     */
    @JsonProperty("IPAMConfig")
    private Network.Ipam.Config ipamConfig;

    /**
     * FIXME verify
     */
    @JsonProperty("Links")
    private List<String> links;

    /**
     * FIXME no docs, unknown field.
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
    public ContainerNetwork withIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    /**
     * @see #ipamConfig
     */
    @CheckForNull
    public Network.Ipam.Config getIpamConfig() {
        return ipamConfig;
    }

    /**
     * @see #ipamConfig
     */
    public ContainerNetwork withIpamConfig(Network.Ipam.Config ipamConfig) {
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
    public List<String> getLinks() {
        return links;
    }

    /**
     * @see #links
     */
    public ContainerNetwork withLinks(List<String> links) {
        this.links = links;
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
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return  EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
