package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;
import java.util.List;

/**
 * @author Kanstantsin Shautsou
 * @see com.github.dockerjava.api.command.UpdateContainerCmd
 * @see <a href="https://docs.docker.com/engine/reference/api/docker_remote_api_v1.22/">
 * https://docs.docker.com/engine/reference/api/docker_remote_api_v1.22/</a>
 * @since {@link RemoteApiVersion#VERSION_1_22}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateContainerResponse extends ResponseItem {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Warnings")
    private List<String> warnings;

    @CheckForNull
    public List<String> getWarnings() {
        return warnings;
    }
}
