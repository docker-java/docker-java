package com.github.dockerjava.jaxrs;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Serializable object that store options for {@link com.github.dockerjava.jaxrs.DockerCmdExecFactoryImpl}
 *
 * @author Kanstantsin Shautsou
 */
public class DockerCmdExecConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer readTimeout = null;

    private Integer connectTimeout = null;

    private Integer maxTotalConnections = null;

    private Integer maxPerRouteConnections = null;

    /**
     * Fill values in builder style with with-methods
     */
    private DockerCmdExecConfig() {
    }

    public static DockerCmdExecConfig create() {
        return new DockerCmdExecConfig();
    }

    @CheckForNull
    public Integer getReadTimeout() {
        return readTimeout;
    }

    public DockerCmdExecConfig withReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    @CheckForNull
    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public DockerCmdExecConfig withConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    @CheckForNull
    public Integer getMaxPerRouteConnections() {
        return maxPerRouteConnections;
    }

    public DockerCmdExecConfig withMaxPerRouteConnections(Integer maxPerRouteConnections) {
        this.maxPerRouteConnections = maxPerRouteConnections;
        return this;
    }

    @CheckForNull
    public Integer getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public DockerCmdExecConfig withMaxTotalConnections(Integer maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DockerCmdExecConfig that = (DockerCmdExecConfig) o;

        if (readTimeout != null ? !readTimeout.equals(that.readTimeout) : that.readTimeout != null) return false;
        if (connectTimeout != null ? !connectTimeout.equals(that.connectTimeout) : that.connectTimeout != null)
            return false;
        if (maxTotalConnections != null ? !maxTotalConnections.equals(that.maxTotalConnections) : that.maxTotalConnections != null)
            return false;
        return !(maxPerRouteConnections != null ? !maxPerRouteConnections.equals(that.maxPerRouteConnections) : that.maxPerRouteConnections != null);

    }

    @Override
    public int hashCode() {
        int result = readTimeout != null ? readTimeout.hashCode() : 0;
        result = 31 * result + (connectTimeout != null ? connectTimeout.hashCode() : 0);
        result = 31 * result + (maxTotalConnections != null ? maxTotalConnections.hashCode() : 0);
        result = 31 * result + (maxPerRouteConnections != null ? maxPerRouteConnections.hashCode() : 0);
        return result;
    }
}
