package com.github.dockerjava.core;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bean to encapsulate the version of the Docker Remote API (REST API)
 * <p>
 * Contains the minor and major version of the API as well as operations to compare API versions.
 *
 * @author Marcus Thiesen
 */
public class RemoteApiVersion {

    public static final RemoteApiVersion VERSION_1_19 = RemoteApiVersion.create(1, 19);

    private static final Pattern VERSION_REGEX = Pattern.compile("v?(\\d+)\\.(\\d+)");

    private static final RemoteApiVersion UNKNOWN_VERSION = new RemoteApiVersion(0, 0) {

        @Override
        public boolean isGreaterOrEqual(final RemoteApiVersion other) {
            return false;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).addValue("UNKNOWN_VERSION").toString();
        }

        @Override
        public String asWebPathPart() {
            return "";
        }
    };

    private final int major;

    private final int minor;

    private RemoteApiVersion(final int major, final int minor) {
        this.major = major;
        this.minor = minor;
    }

    public static RemoteApiVersion create(final int major, final int minor) {
        Preconditions.checkArgument(major > 0, "Major version must be bigger than 0 but is " + major);
        Preconditions.checkArgument(minor > 0, "Minor version must be bigger than 0 but is " + minor);
        return new RemoteApiVersion(major, minor);
    }

    public static RemoteApiVersion unknown() {
        return UNKNOWN_VERSION;
    }

    public static RemoteApiVersion parseConfig(final String version) {
        Preconditions.checkArgument(version != null, "Version must not be null");
        final Matcher matcher = VERSION_REGEX.matcher(version);
        if (matcher.matches()) {
            return create(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        }
        throw new IllegalArgumentException(version + " can not be parsed");
    }

    public static RemoteApiVersion parseConfigWithDefault(final String version) {
        if (Strings.isNullOrEmpty(version)) {
            return UNKNOWN_VERSION;
        }

        try {
            return parseConfig(version);
        } catch (IllegalArgumentException e) {
            return UNKNOWN_VERSION;
        }
    }

    public boolean isGreaterOrEqual(final RemoteApiVersion other) {
        if (major >= other.major && minor >= other.minor) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final RemoteApiVersion that = (RemoteApiVersion) o;
        return Objects.equal(major, that.major) && Objects.equal(minor, that.minor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(major, minor);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("major", major).add("minor", minor).toString();
    }

    public String asWebPathPart() {
        return "v" + major + "." + minor;
    }
}
