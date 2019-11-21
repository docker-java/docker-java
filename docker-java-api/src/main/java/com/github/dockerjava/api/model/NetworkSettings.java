/*
 * Created on 16.01.2016
 */
package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Marcus Linke
 *
 */
@EqualsAndHashCode
@ToString
public class NetworkSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("Bridge")
    private String bridge;

    @FieldName("SandboxID")
    private String sandboxId;

    @FieldName("HairpinMode")
    private Boolean hairpinMode;

    @FieldName("LinkLocalIPv6Address")
    private String linkLocalIPv6Address;

    @FieldName("LinkLocalIPv6PrefixLen")
    private Integer linkLocalIPv6PrefixLen;

    @FieldName("Ports")
    private Ports ports;

    @FieldName("SandboxKey")
    private String sandboxKey;

    @FieldName("SecondaryIPAddresses")
    private Object secondaryIPAddresses;

    @FieldName("SecondaryIPv6Addresses")
    private Object secondaryIPv6Addresses;

    @FieldName("EndpointID")
    private String endpointID;

    @FieldName("Gateway")
    private String gateway;

    @FieldName("PortMapping")
    private Map<String, Map<String, String>> portMapping;

    @FieldName("GlobalIPv6Address")
    private String globalIPv6Address;

    @FieldName("GlobalIPv6PrefixLen")
    private Integer globalIPv6PrefixLen;

    @FieldName("IPAddress")
    private String ipAddress;

    @FieldName("IPPrefixLen")
    private Integer ipPrefixLen;

    @FieldName("IPv6Gateway")
    private String ipV6Gateway;

    @FieldName("MacAddress")
    private String macAddress;

    @FieldName("Networks")
    private Map<String, ContainerNetwork> networks;

    /**
     * @deprecated since {@link RemoteApiVersion#VERSION_1_21}
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @deprecated since {@link RemoteApiVersion#VERSION_1_21}
     */
    public Integer getIpPrefixLen() {
        return ipPrefixLen;
    }

    /**
     * @deprecated since {@link RemoteApiVersion#VERSION_1_21}
     */
    public String getGateway() {
        return gateway;
    }

    public String getBridge() {
        return bridge;
    }

    /**
     * @deprecated since {@link RemoteApiVersion#VERSION_1_21}
     */
    public Map<String, Map<String, String>> getPortMapping() {
        return portMapping;
    }

    /**
     * @deprecated since {@link RemoteApiVersion#VERSION_1_21}
     */
    public String getMacAddress() {
        return macAddress;
    }

    public Ports getPorts() {
        return ports;
    }

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    public String getEndpointID() {
        return endpointID;
    }

    public String getIpV6Gateway() {
        return ipV6Gateway;
    }

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    public Map<String, ContainerNetwork> getNetworks() {
        return networks;
    }

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    public String getSandboxId() {
        return sandboxId;
    }

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    public String getSandboxKey() {
        return sandboxKey;
    }

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    public Object getSecondaryIPAddresses() {
        return secondaryIPAddresses;
    }

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    public Object getSecondaryIPv6Addresses() {
        return secondaryIPv6Addresses;
    }

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    public Boolean getHairpinMode() {
        return hairpinMode;
    }

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    public String getLinkLocalIPv6Address() {
        return linkLocalIPv6Address;
    }

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    public Integer getLinkLocalIPv6PrefixLen() {
        return linkLocalIPv6PrefixLen;
    }

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    public String getGlobalIPv6Address() {
        return globalIPv6Address;
    }

    /**
     * @since {@link RemoteApiVersion#VERSION_1_21}
     */
    public Integer getGlobalIPv6PrefixLen() {
        return globalIPv6PrefixLen;
    }
}
