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

@JsonSerialize(using = VolumeBinds.Serializer.class)
@JsonDeserialize(using = VolumeBinds.Deserializer.class)
public class VolumeBinds {
    private final Bind[] binds;
    private final Volume[] volumes;

    public VolumeBinds(Bind... binds) {
        this.binds = binds;
        this.volumes = new Volume[binds.length];
        for(int i=0; i<volumes.length;i++){
            volumes[i] = binds[i].getVolume();
        }
    }

    public Bind[] getBinds() {
        return binds;
    }

    public Volume[] getVolumes() {
        return volumes;
    }

    public static class Serializer extends JsonSerializer<VolumeBinds> {

        @Override
        public void serialize(VolumeBinds binds, JsonGenerator jsonGen,
                              SerializerProvider serProvider) throws IOException,
                JsonProcessingException {

            jsonGen.writeStartObject();
            for (Bind bind : binds.getBinds()) {
                jsonGen.writeFieldName(bind.getPath());
                jsonGen.writeString(Boolean.toString(!bind.isReadOnly()));
            }
            jsonGen.writeEndObject();
        }

    }

    public static class Deserializer extends JsonDeserializer<VolumeBinds> {
        @Override
        public VolumeBinds deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

            List<Bind> binds = new ArrayList<Bind>();
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);
            for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext();) {

                Map.Entry<String, JsonNode> field = it.next();
                if (!field.getValue().equals(NullNode.getInstance())) {
                    Bind bind = new Bind(field.getValue().toString(),Volume.parse(field.getKey()));
                    binds.add(bind);
                }
            }
            return new VolumeBinds(binds.toArray(new Bind[binds.size()]));
        }
    }

}
