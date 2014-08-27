package com.github.dockerjava.api.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.NullNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// This is not going to be serialized
@JsonDeserialize(using = VolumeBinds.Deserializer.class)
public class VolumeBinds {
    private final VolumeBind[] binds;

    public VolumeBinds(VolumeBind... binds) {
        this.binds = binds;
    }

    public VolumeBind[] getBinds() {
        return binds;
    }

    public static final class Deserializer extends JsonDeserializer<VolumeBinds> {
        @Override
        public VolumeBinds deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

            List<VolumeBind> binds = new ArrayList<VolumeBind>();
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);
            for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext();) {
                Map.Entry<String, JsonNode> field = it.next();
                JsonNode value = field.getValue();
                if (!value.equals(NullNode.getInstance())) {
                    if (!value.isTextual()){
                        throw deserializationContext.mappingException("Expected path for '"+field.getKey()+"'in host but got '"+ value+"'.");
                    }
                    VolumeBind bind = new VolumeBind(value.asText(),field.getKey());
                    binds.add(bind);
                }
            }
            return new VolumeBinds(binds.toArray(new VolumeBind[binds.size()]));
        }
    }

}
