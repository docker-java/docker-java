package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

/**
 * Container restart policy
 *
 * <dl>
 * <dt>no</dt>
 * <dd>Do not restart the container if it dies. (default)</dd>
 *
 * <dt>on-failure</dt>
 * <dd>Restart the container if it exits with a non-zero exit code. Can also accept an optional maximum restart count (e.g. on-failure:5).
 * <dd>
 *
 * <dt>always</dt>
 * <dd>Always restart the container no matter what exit code is returned.
 * <dd>
 * </dl>
 *
 * @author Marcus Linke
 *
 */
@EqualsAndHashCode
public class RestartPolicy implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("MaximumRetryCount")
    private Integer maximumRetryCount = 0;

    @JsonProperty("Name")
    private String name = "";

    public RestartPolicy() {
    }

    private RestartPolicy(int maximumRetryCount, String name) {
        requireNonNull(name, "name is null");
        this.maximumRetryCount = maximumRetryCount;
        this.name = name;
    }

    /**
     * Do not restart the container if it dies. (default)
     */
    public static RestartPolicy noRestart() {
        return new RestartPolicy();
    }

    /**
     * Always restart the container no matter what exit code is returned.
     */
    public static RestartPolicy alwaysRestart() {
        return new RestartPolicy(0, "always");
    }

    /**
     * Restart the container if it exits with a non-zero exit code.
     *
     * @param maximumRetryCount
     *            the maximum number of restarts. Set to <code>0</code> for unlimited retries.
     */
    public static RestartPolicy onFailureRestart(int maximumRetryCount) {
        return new RestartPolicy(maximumRetryCount, "on-failure");
    }

    /**
     * Restart the container unless it has been stopped
     */
    public static RestartPolicy unlessStoppedRestart() {
        return new RestartPolicy(0, "unless-stopped");
    }

    public Integer getMaximumRetryCount() {
        return maximumRetryCount;
    }

    public String getName() {
        return name;
    }

    /**
     * Parses a textual restart polixy specification (as used by the Docker CLI) to a {@link RestartPolicy}.
     *
     * @param serialized
     *            the specification, e.g. <code>on-failure:2</code>
     * @return a {@link RestartPolicy} matching the specification
     * @throws IllegalArgumentException
     *             if the specification cannot be parsed
     */
    public static RestartPolicy parse(String serialized) throws IllegalArgumentException {
        try {
            String[] parts = serialized.split(":");
            String name = parts[0];
            if ("no".equals(name)) {
                return noRestart();
            }

            if ("always".equals(name)) {
                return alwaysRestart();
            }

            if ("unless-stopped".equals(name)) {
                return unlessStoppedRestart();
            }

            if ("on-failure".equals(name)) {
                int count = 0;
                if (parts.length == 2) {
                    count = Integer.parseInt(parts[1]);
                }
                return onFailureRestart(count);
            }
            throw new IllegalArgumentException();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing RestartPolicy '" + serialized + "'");
        }
    }

    /**
     * Returns a string representation of this {@link RestartPolicy}. The format is <code>name[:count]</code>, like the argument in
     * {@link #parse(String)}.
     *
     * @return a string representation of this {@link RestartPolicy}
     */
    @Override
    public String toString() {
        String result = name.isEmpty() ? "no" : name;
        return maximumRetryCount > 0 ? result + ":" + maximumRetryCount : result;
    }
}
