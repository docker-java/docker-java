package com.github.dockerjava.api.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Log driver to use for a created/running container. The available types are:
 *
 * json-file (default) syslog journald none
 *
 * If a driver is specified that is NOT supported,docker will default to null. If configs are supplied that are not supported by the type
 * docker will ignore them. In most cases setting the config option to null will suffice. Consult the docker remote API for a more detailed
 * and up-to-date explanation of the available types and their options.
 */
public class LogConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Type")
    public LoggingType type = null;

    @JsonProperty("Config")
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
    @JsonSerialize(using = LoggingType.Serializer.class)
    public enum LoggingType {
        DEFAULT("json-file"),
        JSON_FILE("json-file"),
        NONE("none"),
        SYSLOG("syslog"),
        JOURNALD("journald"),
        GELF("gelf"),
        FLUENTD("fluentd"),
        AWSLOGS("awslogs"),
        SPLUNK("splunk");

        private String type;

        LoggingType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static final class Serializer extends JsonSerializer<LoggingType> {
            @Override
            public void serialize(LoggingType value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                jgen.writeString(value.getType());
            }
        }

        public static final class Deserializer extends JsonDeserializer<LoggingType> {
            @Override
            public LoggingType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException, JsonProcessingException {

                ObjectCodec oc = jsonParser.getCodec();
                JsonNode node = oc.readTree(jsonParser);

                for (LoggingType loggingType : values()) {
                    if (loggingType.getType().equals(node.asText())) {
                        return loggingType;
                    }
                }

                throw new IllegalArgumentException("No enum constant " + LoggingType.class + "." + node.asText());
            }
        }
    }
}
