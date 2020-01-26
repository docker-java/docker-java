package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Map;

/**
 * Represents a bind mounted volume in a Docker container.
 * <p>
 * Due to an inconsistency in the Docker REST API implementation the response to a container
 * command might include either {@code "Mounts" : [ { "Destination" : "/path/to/mount" } ]} or
 * {@code "Mounts" : [ { "Destination" : { "path" : "/path/to/mount" } } ]} JSON snippets. However,
 * both variants have to be mapped to this class. Therefore, both a single-argument constructor
 * as well as a single-argument factory method is provided either of which handles the former or
 * latter variant (with {@code path} key), respectively.
 *
 * @see Bind
 */
@EqualsAndHashCode
public class Volume implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Handles the {@code { "Destination" : { "path" : "/path/to/mount" } }} variant.
     * @param path the destination path of the bind mounted volume
     * @return a volume instance referring to the given path.
     * @deprecated use {@link #parse(Map)}
     */
    @Nonnull
    @Deprecated
    public static Volume parse(@JsonProperty("path") String path) {
        return new Volume(path);
    }

    /**
     * Handles the {@code { "Destination" : { "path" : "/path/to/mount" } }} variant.
     * @param path the destination path of the bind mounted volume
     * @return a volume instance referring to the given path.
     */
    @Nonnull
    @JsonCreator
    public static Volume parse(Map<String, String> primitive) {
        return new Volume(primitive.get("path"));
    }

    private String path;

    /**
     * Creates a volume referring to the given path.
     * Handles the {@code { "Destination" : "/path/to/mount" }} variant.
     * @param path the destination path of the bind mounted volume
     */
    public Volume(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    @JsonValue
    public String toString() {
        return getPath();
    }
}
