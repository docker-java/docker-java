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
@JsonDeserialize(using = VolumeBinds.Deserializer.class)
@JsonSerialize(using = VolumeBinds.Serializer.class)
public class VolumeBinds implements Serializable {
    private static final long serialVersionUID = 1L;

    private final VolumeBind[] binds;

    public VolumeBinds(VolumeBind... binds) {
        this.binds = binds;
    }

    public VolumeBind[] getBinds() {
        return binds;
    }

    public static final class Serializer extends JsonSerializer<VolumeBinds> {

        @Override
        public void serialize(VolumeBinds value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                JsonProcessingException {
            jgen.writeStartObject();
            for (final VolumeBind bind : value.binds) {
                jgen.writeStringField(bind.getContainerPath(), bind.getHostPath());
            }
            jgen.writeEndObject();
        }
    }

    public static final class Deserializer extends JsonDeserializer<VolumeBinds> {
        @Override
        public VolumeBinds deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {

            List<VolumeBind> binds = new ArrayList<VolumeBind>();
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);
            for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext();) {
                Map.Entry<String, JsonNode> field = it.next();
                JsonNode value = field.getValue();
                if (!value.equals(NullNode.getInstance())) {
                    if (!value.isTextual()) {
                        throw deserializationContext.mappingException("Expected path for '" + field.getKey()
                                + "'in host but got '" + value + "'.");
                    }
                    VolumeBind bind = new VolumeBind(value.asText(), field.getKey());
                    binds.add(bind);
                }
            }
            return new VolumeBinds(binds.toArray(new VolumeBind[binds.size()]));
        }
    }

}
