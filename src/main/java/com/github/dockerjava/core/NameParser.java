/**
 * Copyright (C) 2014 SignalFuse, Inc.
 */
package com.github.dockerjava.core;

import java.util.regex.Pattern;

import com.github.dockerjava.api.model.AuthConfig;

public class NameParser {

    private static final Pattern VALID_HEX_PATTERN = Pattern.compile("^([a-f0-9]{64})$");
    private static final Pattern VALID_NAMESPACE_PATTERN = Pattern.compile("^([a-z0-9_]{4,30})$");
    private static final Pattern VALID_REPO_PATTERN = Pattern.compile("^([a-z0-9-_.]+)$");

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
    }

    public static void validateRepositoryName(String repositoryName) {
        String name;
        String namespace;
        String[] nameParts = repositoryName.split("/", 2);
        if (nameParts.length < 2) {
            namespace = "library";
            name = nameParts[0];
            if (VALID_HEX_PATTERN.matcher(name).matches()) {
                throw new InvalidRepositoryNameException(String.format(
                        "Invalid repository name (%s), cannot specify 64-byte hexadecimal strings",
                        name));
            }
        } else {
            namespace = nameParts[0];
            name = nameParts[1];
        }
        if (!VALID_NAMESPACE_PATTERN.matcher(namespace).matches()) {
            throw new InvalidRepositoryNameException(
                    String.format(
                            "Invalid namespace name (%s), only [a-z0-9_] are allowed, size between 4 and 30",
                            namespace));
        }
        if (!VALID_REPO_PATTERN.matcher(name).matches()) {
            throw new InvalidRepositoryNameException(String.format(
                    "Invalid repository name (%s), only [a-z0-9-_.] are allowed", name));
        }
    }

    public static HostnameReposName resolveRepositoryName(String reposName) {
        if (reposName.contains("://")) {
            // It cannot contain a scheme!
            throw new InvalidRepositoryNameException();
        }

        String[] nameParts = reposName.split("/", 2);
        if (nameParts.length == 1
                || (!nameParts[0].contains(".") && !nameParts[0].contains(":") && !nameParts[0]
                        .equals("localhost"))) {
            return new HostnameReposName(AuthConfig.DEFAULT_SERVER_ADDRESS, reposName);
        }

        String hostname = nameParts[0];
        reposName = nameParts[1];
        if (hostname.contains("index.docker.io")) {
            throw new InvalidRepositoryNameException(String.format(
                    "Invalid repository name, try \"%s\" instead", reposName));
        }

        validateRepositoryName(reposName);
        return new HostnameReposName(hostname, reposName);
    }

    public static class HostnameReposName {
        public final String hostname;
        public final String reposName;

        public HostnameReposName(String hostname, String reposName) {
            this.hostname = hostname;
            this.reposName = reposName;
        }

    }
}
