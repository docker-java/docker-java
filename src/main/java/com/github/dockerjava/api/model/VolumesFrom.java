package com.github.dockerjava.api.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

@JsonSerialize(using = VolumesFrom.Serializer.class)
@JsonDeserialize(using = VolumesFrom.Deserializer.class)
public class VolumesFrom {

	private VolumeFrom[] volumesFrom;

	public VolumesFrom(VolumeFrom... volumesFrom) {
		this.volumesFrom = volumesFrom;
	}

	public VolumeFrom[] getVolumesFrom() {
		return volumesFrom;
	}

	public static class Serializer extends JsonSerializer<VolumesFrom> {

		@Override
		public void serialize(VolumesFrom volumesFrom, JsonGenerator jsonGen,
				SerializerProvider serProvider) throws IOException,
				JsonProcessingException {

			//
			jsonGen.writeStartArray();
			for (VolumeFrom bind : volumesFrom.getVolumesFrom()) {
				jsonGen.writeString(bind.toString());
			}
			jsonGen.writeEndArray();
			//
		}

	}

	public static class Deserializer extends JsonDeserializer<VolumesFrom> {
		@Override
		public VolumesFrom deserialize(JsonParser jsonParser,
				DeserializationContext deserializationContext)
				throws IOException, JsonProcessingException {

			List<VolumeFrom> volumesFrom = new ArrayList<VolumeFrom>();
			ObjectCodec oc = jsonParser.getCodec();
			JsonNode node = oc.readTree(jsonParser);
			for (Iterator<JsonNode> it = node.iterator(); it.hasNext();) {
				JsonNode field = it.next();
				volumesFrom.add(VolumeFrom.parse(field.asText()));
			}
			return new VolumesFrom(volumesFrom.toArray(new VolumeFrom[0]));
		}
	}

}
