package com.github.dockerjava.api.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

// This is not going to be serialized
@JsonSerialize(using = VolumesRW.Serializer.class)
@JsonDeserialize(using = VolumesRW.Deserializer.class)
public class VolumesRW implements Serializable {
    private static final long serialVersionUID = 1L;

    private final VolumeRW[] volumesRW;

    public VolumesRW(VolumeRW... binds) {
        this.volumesRW = binds;
    }

    public VolumeRW[] getVolumesRW() {
        return volumesRW;
    }

    public static final class Serializer extends JsonSerializer<VolumesRW> {

        @Override
        public void serialize(VolumesRW value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                JsonProcessingException {
            jgen.writeStartObject();
            for (final VolumeRW volumeRW : value.volumesRW) {
                jgen.writeBooleanField(volumeRW.getVolume().getPath(), volumeRW.getAccessMode().toBoolean());
            }
            jgen.writeEndObject();
        }

    }

    public static final class Deserializer extends JsonDeserializer<VolumesRW> {
        @Override
        public VolumesRW deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {

            List<VolumeRW> volumesRW = new ArrayList<VolumeRW>();
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);

            for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext();) {
                Map.Entry<String, JsonNode> field = it.next();
                JsonNode value = field.getValue();

                if (!value.equals(NullNode.getInstance())) {
                    if (!value.isBoolean()) {
                        throw deserializationContext.mappingException("Expected access mode for '" + field.getKey()
                                + "' in host but got '" + value + "'.");
                    }

                    VolumeRW bind = new VolumeRW(new Volume(field.getKey()), AccessMode.fromBoolean(value.asBoolean()));
                    volumesRW.add(bind);
                }
            }
            return new VolumesRW(volumesRW.toArray(new VolumeRW[volumesRW.size()]));
        }
    }

}
