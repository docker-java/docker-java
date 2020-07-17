package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * Log driver to use for a created/running container. The available types are:
 * <p>
 * json-file (default) syslog journald none
 * <p>
 * If a driver is specified that is NOT supported,docker will default to null. If configs are supplied that are not supported by the type
 * docker will ignore them. In most cases setting the config option to null will suffice. Consult the docker remote API for a more detailed
 * and up-to-date explanation of the available types and their options.
 */
@EqualsAndHashCode
@ToString
public class LogConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String NONE = "none";
    public static final String DEFAULT = "json-file";
    public static final String LOCAL = "local";
    public static final String ETWLOGS = "etwlogs";
    public static final String JSON_FILE = "json-file";
    public static final String SYSLOG = "syslog";
    public static final String JOURNALD = "journald";
    public static final String GELF = "gelf";
    public static final String FLUENTD = "fluentd";
    public static final String AWSLOGS = "awslogs";
    public static final String DB = "db"; // Synology specific driver
    public static final String SPLUNK = "splunk";
    public static final String GCPLOGS = "gcplogs";

    @Deprecated
    public LoggingType type = null;
    private String loggingType = null;

    @JsonProperty("Config")
    public Map<String, String> config;

    @Deprecated
    public LogConfig(LoggingType type, Map<String, String> config) {
        this.config = config;
        this.type = type;
    }

    @Deprecated
    public LogConfig(LoggingType type) {
        this(type, null);
    }

    public LogConfig(String type, Map<String, String> config) {
        this.config = config;
        this.withLoggingType(type);
    }

    public LogConfig(String type) {
        this(type, null);
    }

    public LogConfig() {
    }

    @Deprecated
    @JsonIgnore
    public LoggingType getType() {
        return type;
    }

    @Deprecated
    @JsonIgnore
    public LogConfig setType(LoggingType type) {
        this.type = type;
        return this;
    }

    @JsonGetter("Type")
    public String getLoggingType() {
        if (type != null) {
            return type.getType();
        }

        return loggingType;
    }

    @JsonSetter("Type")
    public LogConfig withLoggingType(String loggingType) {
        for (LoggingType enumType : LoggingType.values()) {
            if (enumType.getType().equals(loggingType)) {
                this.type = enumType;
            }
        }

        this.loggingType = loggingType;
        return this;
    }

    @JsonIgnore
    public Map<String, String> getConfig() {
        return config;
    }

    @JsonIgnore
    @Deprecated
    public LogConfig setConfig(Map<String, String> config) {
        this.config = config;
        return this;
    }

    @JsonIgnore
    public LogConfig withConfig(Map<String, String> config) {
        this.config = config;
        return this;
    }

    @Deprecated
    public enum LoggingType {
        NONE("none"),
        DEFAULT("json-file"),
        LOCAL("local"),
        ETWLOGS("etwlogs"),
        JSON_FILE("json-file"),
        SYSLOG("syslog"),
        JOURNALD("journald"),
        GELF("gelf"),
        FLUENTD("fluentd"),
        AWSLOGS("awslogs"),
        DB("db"), // Synology specific driver
        SPLUNK("splunk"),
        GCPLOGS("gcplogs");

        private String type;

        LoggingType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return String.valueOf(type);
        }
    }
}
