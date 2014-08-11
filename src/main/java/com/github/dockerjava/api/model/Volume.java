package com.github.dockerjava.api.model;

import java.io.IOException;
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

@JsonDeserialize(using = Volume.Deserializer.class)
@JsonSerialize(using = Volume.Serializer.class)
public class Volume {

	private String path;
	
	private boolean readWrite = true;

	public Volume(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
	
	public boolean isReadWrite() {
		return readWrite;
	}

	public static Volume parse(String serialized) {
		return new Volume(serialized);
	}
	
	@Override
	public String toString() {
		return getPath();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Volume) {
			Volume other = (Volume) obj;
			return new EqualsBuilder().append(path, other.getPath()).append(readWrite, other.isReadWrite())
					.isEquals();
		} else
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(path).append(readWrite).toHashCode();
	}
	
	public static class Serializer extends JsonSerializer<Volume> {

		@Override
		public void serialize(Volume volume, JsonGenerator jsonGen,
				SerializerProvider serProvider) throws IOException,
				JsonProcessingException {

			jsonGen.writeStartObject();
			jsonGen.writeFieldName(volume.getPath());
			jsonGen.writeString(Boolean.toString(volume.isReadWrite()));
			jsonGen.writeEndObject();
		}

	}

	public static class Deserializer extends JsonDeserializer<Volume> {
		@Override
		public Volume deserialize(JsonParser jsonParser,
				DeserializationContext deserializationContext)
				throws IOException, JsonProcessingException {
			ObjectCodec oc = jsonParser.getCodec();
			JsonNode node = oc.readTree(jsonParser);
			if (!node.equals(NullNode.getInstance())) {
				Entry<String, JsonNode> field = node.fields().next();
				return Volume.parse(field.getKey());
			} else {
				return null;
			}
		}
	}

	

}
