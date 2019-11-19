package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
@ToString
@SuppressWarnings("checkstyle:hideutilityclassconstructor")
public class ServiceGlobalModeOptions implements Serializable {
    public static final long serialVersionUID = 1L;

    // Intentionally left blank, there are no options for this mode
}
