package com.github.dockerjava.core;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bean to encapsulate the version of the <a href="http://docs.docker.com/engine/reference/api/docker_remote_api/">Docker Remote (REST)
 * API</a>
 * <p/>
 * Contains the minor and major version of the API as well as operations to compare API versions.
 *
 * @author Marcus Thiesen
 */
public class RemoteApiVersion implements Serializable {
    private static final long serialVersionUID = -5382212999262115459L;

    private static final Pattern VERSION_REGEX = Pattern.compile("v?(\\d+)\\.(\\d+)");

    /**
     * Online documentation is not available anymore.
     */
    public static final RemoteApiVersion VERSION_1_7 = RemoteApiVersion.create(1, 7);

    /**
     * @see <a href="http://docs.docker.com/engine/reference/api/docker_remote_api_v1.16/">Docker API 1.16</a>
     */
    public static final RemoteApiVersion VERSION_1_16 = RemoteApiVersion.create(1, 16);

    /**
     * @see <a href="http://docs.docker.com/engine/reference/api/docker_remote_api_v1.17/">Docker API 1.17</a>
     */
    public static final RemoteApiVersion VERSION_1_17 = RemoteApiVersion.create(1, 17);

    /**
     * @see <a href="http://docs.docker.com/engine/reference/api/docker_remote_api_v1.18/">Docker API 1.18</a>
     */
    public static final RemoteApiVersion VERSION_1_18 = RemoteApiVersion.create(1, 18);

    /**
     * @see <a href="http://docs.docker.com/engine/reference/api/docker_remote_api_v1.19/">Docker API 1.19</a>
     */
    public static final RemoteApiVersion VERSION_1_19 = RemoteApiVersion.create(1, 19);

    /**
     * @see <a href="http://docs.docker.com/engine/reference/api/docker_remote_api_v1.20/">Docker API 1.20</a>
     */
    public static final RemoteApiVersion VERSION_1_20 = RemoteApiVersion.create(1, 20);

    /**
     * @see <a href="http://docs.docker.com/engine/reference/api/docker_remote_api_v1.21/">Docker API 1.21</a>
     */
    public static final RemoteApiVersion VERSION_1_21 = RemoteApiVersion.create(1, 21);

    /**
     * @see <a href="https://github.com/docker/docker/blob/master/docs/reference/api/docker_remote_api_v1.22.md">Docker API 1.22</a>
     */
    public static final RemoteApiVersion VERSION_1_22 = RemoteApiVersion.create(1, 22);

    /**
     * @see <a href="https://github.com/docker/docker/blob/master/docs/reference/api/docker_remote_api_v1.23.md">Docker API 1.23</a>
     */
    public static final RemoteApiVersion VERSION_1_23 = RemoteApiVersion.create(1, 23);

    /**
     * @see <a href="https://github.com/docker/docker/blob/master/docs/reference/api/docker_remote_api_v1.24.md">Docker API 1.24</a>
     */
    public static final RemoteApiVersion VERSION_1_24 = RemoteApiVersion.create(1, 24);

    /*
     * @see <a href="https://docs.docker.com/engine/api/v1.25/">Docker API 1.25</a>
     */
    public static final RemoteApiVersion VERSION_1_25 = RemoteApiVersion.create(1, 25);

    public static final RemoteApiVersion VERSION_1_26 = RemoteApiVersion.create(1, 26);
    public static final RemoteApiVersion VERSION_1_27 = RemoteApiVersion.create(1, 27);
    public static final RemoteApiVersion VERSION_1_28 = RemoteApiVersion.create(1, 28);
    public static final RemoteApiVersion VERSION_1_29 = RemoteApiVersion.create(1, 29);
    public static final RemoteApiVersion VERSION_1_30 = RemoteApiVersion.create(1, 30);
    public static final RemoteApiVersion VERSION_1_31 = RemoteApiVersion.create(1, 31);
    public static final RemoteApiVersion VERSION_1_32 = RemoteApiVersion.create(1, 32);
    public static final RemoteApiVersion VERSION_1_33 = RemoteApiVersion.create(1, 33);
    public static final RemoteApiVersion VERSION_1_34 = RemoteApiVersion.create(1, 34);
    public static final RemoteApiVersion VERSION_1_35 = RemoteApiVersion.create(1, 35);
    public static final RemoteApiVersion VERSION_1_36 = RemoteApiVersion.create(1, 36);
    public static final RemoteApiVersion VERSION_1_37 = RemoteApiVersion.create(1, 37);
    public static final RemoteApiVersion VERSION_1_38 = RemoteApiVersion.create(1, 38);
    public static final RemoteApiVersion VERSION_1_40 = RemoteApiVersion.create(1, 40);


    /**
     * Unknown, docker doesn't reflect reality. I.e. we implemented method, but for javadoc it not clear when it was added.
     */
    public static final RemoteApiVersion UNKNOWN_VERSION = new RemoteApiVersion(0, 0) {

        @Override
        public boolean isGreaterOrEqual(final RemoteApiVersion other) {
            return false;
        }

        @Override
        public boolean isGreater(RemoteApiVersion other) {
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

    public boolean isGreater(final RemoteApiVersion other) {
        return major > other.major || (major == other.major && minor > other.minor);

    }

    /**
     * @return String representation of version. i.e. "1.22"
     */
    public String getVersion() {
        return major + "." + minor;
    }

    // CHECKSTYLE:OFF
    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final RemoteApiVersion that = (RemoteApiVersion) o;
        return Objects.equal(major, that.major) && Objects.equal(minor, that.minor);
    }

    // CHECKSTYLE:ON

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
