package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FromPrimitive;
import com.github.dockerjava.api.annotation.FieldName;
import com.github.dockerjava.api.annotation.ToPrimitive;

import javax.annotation.CheckForNull;
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
public class LogConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("Type")
    public LoggingType type = null;

    @FieldName("Config")
    public Map<String, String> config;

    public LogConfig(LoggingType type, Map<String, String> config) {
        this.type = type;
        this.config = config;
    }

    public LogConfig(LoggingType type) {
        this(type, null);
    }

    public LogConfig() {
    }

    public LoggingType getType() {
        return type;
    }

    public LogConfig setType(LoggingType type) {
        this.type = type;
        return this;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public LogConfig setConfig(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public enum LoggingType {
        NONE("none"),
        DEFAULT("json-file"),
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

        @ToPrimitive
        public String getType() {
            return type;
        }

        @FromPrimitive
        @CheckForNull
        public static LoggingType fromValue(String text) {
            for (LoggingType b : LoggingType.values()) {
                if (String.valueOf(b.type).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return String.valueOf(type);
        }
    }
}
