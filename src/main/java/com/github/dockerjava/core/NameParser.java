/**
 * Copyright (C) 2014 SignalFuse, Inc.
 */
package com.github.dockerjava.core;

import java.util.regex.Pattern;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.core.exception.InvalidRepositoryNameException;

@SuppressWarnings(value = "checkstyle:equalshashcode")
public class NameParser {
    private NameParser() {
    }

    // CHECKSTYLE:OFF
    private static final int RepositoryNameTotalLengthMax = 255;

    private static final Pattern RepositoryNameComponentRegexp = Pattern.compile("[a-z0-9]+(?:[._-][a-z0-9]+)*");

    private static final Pattern RepositoryNameComponentAnchoredRegexp = Pattern.compile("^"
            + RepositoryNameComponentRegexp.pattern() + "$");

    // CHECKSTYLE:ON

    // private static final Pattern RepositoryNameRegexp = Pattern.compile("(?:" +
    // RepositoryNameComponentRegexp.pattern()
    // + "/)*" + RepositoryNameComponentRegexp.pattern());

    public static ReposTag parseRepositoryTag(String name) {
        int n = name.lastIndexOf(':');
        if (n < 0) {
            return new ReposTag(name, "");
        }
        String tag = name.substring(n + 1);
        if (!tag.contains("/")) {
            return new ReposTag(name.substring(0, n), tag);
        }
        return new ReposTag(name, "");
    }

    public static class ReposTag {
        public final String repos;

        public final String tag;

        public ReposTag(String repos, String tag) {
            this.repos = repos;
            this.tag = tag;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ReposTag) {
                ReposTag other = (ReposTag) obj;
                return new EqualsBuilder().append(repos, other.repos).append(tag, other.tag).isEquals();
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
        }
    }

    /*
     * see https://github.com/docker/distribution/blob/master/registry/api/v2/names.go
     */
    public static void validateRepoName(String name) {
        if (name.isEmpty()) {
            throw new InvalidRepositoryNameException(String.format("Invalid empty repository name \"%s\"", name));
        }

        if (name.length() > RepositoryNameTotalLengthMax) {
            throw new InvalidRepositoryNameException(String.format("Repository name \"%s\" is longer than "
                    + RepositoryNameTotalLengthMax, name));
        }

        String[] components = name.split("/");

        for (String component : components) {
            if (!RepositoryNameComponentAnchoredRegexp.matcher(component).matches()) {
                throw new InvalidRepositoryNameException(String.format(
                        "Repository name \"%s\" is invalid. Component: %s", name, component));
            }
        }
    }

    public static HostnameReposName resolveRepositoryName(String reposName) {
        if (reposName.contains("://")) {
            // It cannot contain a scheme!
            throw new InvalidRepositoryNameException();
        }

        String[] nameParts = reposName.split("/", 2);
        if (nameParts.length == 1
                || (!nameParts[0].contains(".") && !nameParts[0].contains(":") && !nameParts[0].equals("localhost"))) {
            return new HostnameReposName(AuthConfig.DEFAULT_SERVER_ADDRESS, reposName);
        }

        String hostname = nameParts[0];
        reposName = nameParts[1];
        if (hostname.contains("index.docker.io")) {
            throw new InvalidRepositoryNameException(String.format("Invalid repository name, try \"%s\" instead",
                    reposName));
        }

        validateRepoName(reposName);
        return new HostnameReposName(hostname, reposName);
    }

    public static class HostnameReposName {
        public final String hostname;

        public final String reposName;

        public HostnameReposName(String hostname, String reposName) {
            this.hostname = hostname;
            this.reposName = reposName;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof HostnameReposName) {
                HostnameReposName other = (HostnameReposName) obj;
                return new EqualsBuilder().append(hostname, other.hostname).append(reposName, other.reposName)
                        .isEquals();
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
        }

    }
}
