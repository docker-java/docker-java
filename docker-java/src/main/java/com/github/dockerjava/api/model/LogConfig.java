package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

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

    @JsonProperty("Type")
    public Type type = null;

    @JsonProperty("Config")
    public Map<String, String> config;

    public LogConfig(Type type, Map<String, String> config) {
        this.type = type;
        this.config = config;
    }

    public LogConfig(Type type) {
        this(type, null);
    }

    public LogConfig() {
    }

    public Type getType() {
        return type;
    }

    public LogConfig setType(Type type) {
        this.type = type;
        return this;
    }

    @JsonIgnore
    public Map<String, String> getConfig() {
        return config;
    }

    @JsonIgnore
    public LogConfig setConfig(Map<String, String> config) {
        this.config = config;
        return this;
    }

    @JsonDeserialize(using = LoggingType.Deserializer.class)
    public interface Type {
        @JsonValue
        String getType();

        final class Deserializer extends JsonDeserializer<Type> {
            @Override
            public Type deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException, JsonProcessingException {

                ObjectCodec oc = jsonParser.getCodec();
                JsonNode node = oc.readTree(jsonParser);

                for (LoggingType loggingType : LoggingType.values()) {
                    if (loggingType.getType().equals(node.asText())) {
                        return loggingType;
                    }
                }

                return new CustomLoggingType(node.asText());
            }
        }
    }

    public enum LoggingType implements Type {
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

        @Override
        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return String.valueOf(type);
        }
    }

    public static class CustomLoggingType implements Type {
        private String type;

        public CustomLoggingType(String type) {
            this.type = type;
        }

        @Override
        public String getType() {
            return this.type;
        }

        @Override
        public String toString() {
            return String.valueOf(type);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Type)) return false;
            Type that = (Type) o;
            return getType().equals(that.getType());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getType());
        }
    }
}
