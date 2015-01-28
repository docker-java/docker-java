package com.github.dockerjava.api.model;

import java.io.IOException;
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


@JsonSerialize(using = Volumes.Serializer.class)
@JsonDeserialize(using = Volumes.Deserializer.class)
public class Volumes {

	private Volume[] volumes;

	public Volumes(Volume... volumes) {
		this.volumes = volumes;
	}
	
	public Volumes(List<Volume> volumes) {
		this.volumes = volumes.toArray(new Volume[volumes.size()]);
	}

	public Volume[] getVolumes() {
		return volumes;
	}

	public static class Serializer extends JsonSerializer<Volumes> {

		@Override
		public void serialize(Volumes volumes, JsonGenerator jsonGen,
				SerializerProvider serProvider) throws IOException,
				JsonProcessingException {
			
			jsonGen.writeStartObject();
			for (Volume volume : volumes.getVolumes()) {
				jsonGen.writeFieldName(volume.getPath());
				jsonGen.writeString(Boolean.toString(volume.getAccessMode().equals(AccessMode.rw) ? true: false));
			}
			jsonGen.writeEndObject();
		}

	}
	
	public static class Deserializer extends JsonDeserializer<Volumes> {
        @Override
        public Volumes deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        	List<Volume> volumes = new ArrayList<Volume>();
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);
            for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext();) {

                Map.Entry<String, JsonNode> field = it.next();
                if (!field.getValue().equals(NullNode.getInstance())) {
                	Volume volume = Volume.parse(field.getKey());
                	volumes.add(volume);
                }
            }
            return new Volumes(volumes.toArray(new Volume[0]));
        }
    }

}
