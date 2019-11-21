package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import com.github.dockerjava.api.command.ListContainersCmd;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Used for Listing containers.
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
@EqualsAndHashCode
@ToString
public class Container implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("Command")
    private String command;

    @FieldName("Created")
    private Long created;

    @FieldName("Id")
    private String id;

    @FieldName("Image")
    private String image;

    /**
     * @since since {@link RemoteApiVersion#VERSION_1_21}
     */
    @FieldName("ImageID")
    private String imageId;

    @FieldName("Names")
    private String[] names;

    @FieldName("Ports")
    public ContainerPort[] ports;

    @FieldName("Labels")
    public Map<String, String> labels;

    @FieldName("Status")
    private String status;

    /**
     * @since ~{@link RemoteApiVersion#VERSION_1_23}
     */
    @FieldName("State")
    private String state;

    /**
     * @since ~{@link RemoteApiVersion#VERSION_1_19}
     */
    @FieldName("SizeRw")
    private Long sizeRw;

    /**
     * Returns only when {@link ListContainersCmd#withShowSize(java.lang.Boolean)} set
     *
     * @since ~{@link RemoteApiVersion#VERSION_1_19}
     */
    @FieldName("SizeRootFs")
    private Long sizeRootFs;

    /**
     * @since ~{@link RemoteApiVersion#VERSION_1_20}
     */
    @FieldName("HostConfig")
    private ContainerHostConfig hostConfig;

    /**
     * Docker API docs says "list of networks", but json names `networkSettings`.
     * So, reusing existed NetworkSettings model object.
     *
     * @since ~{@link RemoteApiVersion#VERSION_1_22}
     */
    @FieldName("NetworkSettings")
    private ContainerNetworkSettings networkSettings;

    /**
     * @since ~{@link RemoteApiVersion#VERSION_1_23}
     */
    @FieldName("Mounts")
    private List<ContainerMount> mounts;

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

    public String getState() {
        return state;
    }

    public ContainerPort[] getPorts() {
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
    public ContainerNetworkSettings getNetworkSettings() {
        return networkSettings;
    }

    /**
     * @see #hostConfig
     */
    @CheckForNull
    public ContainerHostConfig getHostConfig() {
        return hostConfig;
    }

    public List<ContainerMount> getMounts() {
        return mounts;
    }
}
