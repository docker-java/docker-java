package com.github.dockerjava.api.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang.BooleanUtils.isNotTrue;
import static org.apache.commons.lang.StringUtils.isEmpty;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@JsonInclude(Include.NON_NULL)
public class Device implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("CgroupPermissions")
    private String cGroupPermissions = "";

    @JsonProperty("PathOnHost")
    private String pathOnHost = null;

    @JsonProperty("PathInContainer")
    private String pathInContainer = null;

    public Device() {
    }

    public Device(String cGroupPermissions, String pathInContainer, String pathOnHost) {
        checkNotNull(cGroupPermissions, "cGroupPermissions is null");
        checkNotNull(pathInContainer, "pathInContainer is null");
        checkNotNull(pathOnHost, "pathOnHost is null");
        this.cGroupPermissions = cGroupPermissions;
        this.pathInContainer = pathInContainer;
        this.pathOnHost = pathOnHost;
    }

    public String getcGroupPermissions() {
        return cGroupPermissions;
    }

    public String getPathInContainer() {
        return pathInContainer;
    }

    public String getPathOnHost() {
        return pathOnHost;
    }

    /**
     * @link https://github.com/docker/docker/blob/6b4a46f28266031ce1a1315f17fb69113a06efe1/runconfig/opts/parse_test.go#L468
     */
    @Nonnull
    public static Device parse(@Nonnull String deviceStr) {
        String src = "";
        String dst = "";
        String permissions = "rwm";
        final String[] arr = deviceStr.trim().split(":");
        // java String.split() returns wrong length, use tokenizer instead
        switch (new StringTokenizer(deviceStr, ":").countTokens()) {
            case 3: {
                // Mismatches docker code logic. While there is no validations after parsing, checking heregit
                if (validDeviceMode(arr[2])) {
                    permissions = arr[2];
                } else {
                    throw new IllegalArgumentException("Invalid device specification: " + deviceStr);
                }
            }
            case 2: {
                if (validDeviceMode(arr[1])) {
                    permissions = arr[1];
                } else {
                    dst = arr[1];
                }
            }
            case 1: {
                src = arr[0];
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid device specification: " + deviceStr);
            }
        }

        if (isEmpty(dst)) {
            dst = src;
        }

        return new Device(permissions, dst, src);
    }

    /**
     * ValidDeviceMode checks if the mode for device is valid or not.
     * Valid mode is a composition of r (read), w (write), and m (mknod).
     *
     * @link https://github.com/docker/docker/blob/6b4a46f28266031ce1a1315f17fb69113a06efe1/runconfig/opts/parse.go#L796
     */
    private static boolean validDeviceMode(String deviceMode) {
        Map<String, Boolean> validModes = new HashMap<>(3);
        validModes.put("r", true);
        validModes.put("w", true);
        validModes.put("m", true);

        if (isEmpty(deviceMode)) {
            return false;
        }

        for (char ch : deviceMode.toCharArray()) {
            final String mode = String.valueOf(ch);
            if (isNotTrue(validModes.get(mode))) {
                return false; // wrong mode
            }
            validModes.put(mode, false);
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Device) {
            Device other = (Device) obj;
            return new EqualsBuilder().append(cGroupPermissions, other.getcGroupPermissions())
                    .append(pathInContainer, other.getPathInContainer()).append(pathOnHost, other.getPathOnHost())
                    .isEquals();
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(cGroupPermissions).append(pathInContainer).append(pathOnHost).toHashCode();
    }

}
