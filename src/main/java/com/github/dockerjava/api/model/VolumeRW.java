package com.github.dockerjava.api.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map.Entry;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.dockerjava.core.RemoteApiVersion;

/**
 * Represents a bind mounted volume in a Docker container.
 *
 * @see Bind
 * @deprecated since {@link RemoteApiVersion#VERSION_1_20}
 */
@JsonDeserialize(using = VolumeRW.Deserializer.class)
@JsonSerialize(using = VolumeRW.Serializer.class)
@Deprecated
public class VolumeRW implements Serializable {
    private static final long serialVersionUID = 1L;

    private Volume volume;

    private AccessMode accessMode = AccessMode.rw;

    public VolumeRW(Volume volume) {
        this.volume = volume;
    }

    public VolumeRW(Volume volume, AccessMode accessMode) {
        this.volume = volume;
        this.accessMode = accessMode;
    }

    public Volume getVolume() {
        return volume;
    }

    public AccessMode getAccessMode() {
        return accessMode;
    }

    /**
     * Returns a string representation of this {@link VolumeRW} suitable for inclusion in a JSON message. The returned String is simply the
     * container path, {@link #getPath()}.
     *
     * @return a string representation of this {@link VolumeRW}
     */
    @Override
    public String toString() {
        return getVolume() + ":" + getAccessMode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VolumeRW) {
            VolumeRW other = (VolumeRW) obj;
            return new EqualsBuilder().append(getVolume(), other.getVolume()).append(accessMode, other.getAccessMode())
                    .isEquals();
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getVolume()).append(getAccessMode()).toHashCode();
    }

    public static class Serializer extends JsonSerializer<VolumeRW> {

        @Override
        public void serialize(VolumeRW volumeRW, JsonGenerator jsonGen, SerializerProvider serProvider)
                throws IOException, JsonProcessingException {

            jsonGen.writeStartObject();
            jsonGen.writeFieldName(volumeRW.getVolume().getPath());
            jsonGen.writeString(Boolean.toString(volumeRW.getAccessMode().toBoolean()));
            jsonGen.writeEndObject();
        }

    }

    public static class Deserializer extends JsonDeserializer<VolumeRW> {
        @Override
        public VolumeRW deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);
            if (!node.equals(NullNode.getInstance())) {
                Entry<String, JsonNode> field = node.fields().next();
                String volume = field.getKey();
                AccessMode accessMode = AccessMode.fromBoolean(field.getValue().asBoolean());
                return new VolumeRW(new Volume(volume), accessMode);
            } else {
                return null;
            }
        }
    }

}
