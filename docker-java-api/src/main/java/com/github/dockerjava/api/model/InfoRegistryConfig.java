package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @since ~{@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InfoRegistryConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("IndexConfigs")
    private Map<String, IndexConfig> indexConfigs;

    @JsonProperty("InsecureRegistryCIDRs")
    private List<String> insecureRegistryCIDRs;

    /**
     * //FIXME unknown field
     */
    @JsonProperty("Mirrors")
    private Object mirrors;

    /**
     * @see #indexConfigs
     */
    @CheckForNull
    public Map<String, IndexConfig> getIndexConfigs() {
        return indexConfigs;
    }

    /**
     * @see #indexConfigs
     */
    public InfoRegistryConfig withIndexConfigs(Map<String, IndexConfig> indexConfigs) {
        this.indexConfigs = indexConfigs;
        return this;
    }

    /**
     * @see #insecureRegistryCIDRs
     */
    @CheckForNull
    public List<String> getInsecureRegistryCIDRs() {
        return insecureRegistryCIDRs;
    }

    /**
     * @see #insecureRegistryCIDRs
     */
    public InfoRegistryConfig withInsecureRegistryCIDRs(List<String> insecureRegistryCIDRs) {
        this.insecureRegistryCIDRs = insecureRegistryCIDRs;
        return this;
    }

    /**
     * @see #mirrors
     */
    @CheckForNull
    public Object getMirrors() {
        return mirrors;
    }

    /**
     * @see #mirrors
     */
    public InfoRegistryConfig withMirrors(Object mirrors) {
        this.mirrors = mirrors;
        return this;
    }

    @Override
    public String toString() {
        return "InfoRegistryConfig{" +
                "indexConfigs=" + indexConfigs +
                ", insecureRegistryCIDRs=" + insecureRegistryCIDRs +
                ", mirrors=" + mirrors +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfoRegistryConfig that = (InfoRegistryConfig) o;
        return Objects.equals(indexConfigs, that.indexConfigs) &&
                Objects.equals(insecureRegistryCIDRs, that.insecureRegistryCIDRs) &&
                Objects.equals(mirrors, that.mirrors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indexConfigs, insecureRegistryCIDRs, mirrors);
    }

    /**
     * @since ~{@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class IndexConfig {
        @JsonProperty("Mirrors")
        private List<String> mirrors;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Official")
        private Boolean official;

        @JsonProperty("Secure")
        private Boolean secure;

        /**
         * @see #mirrors
         */
        @CheckForNull
        public List<String> getMirrors() {
            return mirrors;
        }

        /**
         * @see #mirrors
         */
        public IndexConfig withMirrors(List<String> mirrors) {
            this.mirrors = mirrors;
            return this;
        }

        /**
         * @see #name
         */
        @CheckForNull
        public String getName() {
            return name;
        }

        /**
         * @see #name
         */
        public IndexConfig withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * @see #official
         */
        @CheckForNull
        public Boolean getOfficial() {
            return official;
        }

        /**
         * @see #official
         */
        public IndexConfig withOfficial(Boolean official) {
            this.official = official;
            return this;
        }

        /**
         * @see #secure
         */
        @CheckForNull
        public Boolean getSecure() {
            return secure;
        }

        /**
         * @see #secure
         */
        public IndexConfig withSecure(Boolean secure) {
            this.secure = secure;
            return this;
        }

        @Override
        public String toString() {
            return "IndexConfig{" +
                    "mirrors=" + mirrors +
                    ", name='" + name + '\'' +
                    ", official=" + official +
                    ", secure=" + secure +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IndexConfig that = (IndexConfig) o;
            return Objects.equals(mirrors, that.mirrors) &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(official, that.official) &&
                    Objects.equals(secure, that.secure);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mirrors, name, official, secure);
        }
    }
}
