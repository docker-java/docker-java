package com.github.dockerjava.core;

import com.github.dockerjava.api.model.Identifier;
import com.github.dockerjava.api.model.Repository;
import org.apache.commons.lang.StringUtils;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.net.URI;

/**
 * Helper object wrapper around `[registryHost/][namespace/]name[:tag][@digest]`
 *
 * @author Kanstantsin Shautsou
 */
public class DockerImageHelper {
    public static final String DEFAULT_NAMESPACE = "library";
    // unix registries: https://github.com/docker/docker/blob/master/registry/config_unix.go#L5-L10
    public static final String DEFAULT_V2_REGISTRY = "https://registry-1.docker.io";
    public static final String DEFAULT_V1_REGISTRY = "https://index.docker.io";

    /**
     * i.e. https://mydomain.com:5000 or http://mydomain.com
     */
    @CheckForNull
    private URI registryHost;

    /**
     * Real prefix before {@see name}. I.e. user name or repository with user name
     * `project/user`
     */
    @CheckForNull
    private String namespace;

    /**
     * Real image name
     */
    private String name;

    /**
     *
     */
    @CheckForNull
    private String tag;

    /**
     * digest part
     */
    @CheckForNull
    private String digest;


    /**
     * @return effective namespace like in docker-client
     */
    @Nonnull
    public String getEffectiveNamespace() {
        if (StringUtils.isNotBlank(namespace)) {
            return DEFAULT_NAMESPACE;
        } else {
            return namespace;
        }
    }

    public String getNamespace() {
        return namespace;
    }

    /**
     * Effective V2 unix registry.
     * TODO support V1 registry? some enum?
     */
    public String getEffectiveRegistryHost() {
        if (registryHost != null) {
            return registryHost.toString();
        } else {
            return DEFAULT_V2_REGISTRY;
        }
    }

    // other helper methods required for docker-java API usage

    public Repository getRepository() {
        return new Repository(registryHost.toString() + namespace + name);
    }

    /**
     * filled identifier
     */
    public Identifier getIdentifier() {
        return new Identifier(getRepository(), tag);
    }

}
