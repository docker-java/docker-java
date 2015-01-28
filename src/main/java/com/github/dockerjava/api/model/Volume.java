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

/**
 * Represents a bind mounted volume in a Docker container.
 * 
 * @see Bind
 */
@JsonDeserialize(using = Volume.Deserializer.class)
@JsonSerialize(using = Volume.Serializer.class)
public class Volume {

	private String path;
	
	private AccessMode accessMode = AccessMode.rw;

	public Volume(String path) {
		this.path = path;
	}
	
	public Volume(String path, AccessMode accessMode) {
		this.path = path;
		this.accessMode = accessMode;
	}

	public String getPath() {
		return path;
	}
	
	public AccessMode getAccessMode() {
		return accessMode;
	}

	public static Volume parse(String serialized) {
		return new Volume(serialized);
	}
	
	/**
	 * Returns a string representation of this {@link Volume} suitable
	 * for inclusion in a JSON message.
	 * The returned String is simply the container path, {@link #getPath()}. 
	 * 
	 * @return a string representation of this {@link Volume}
	 */
	@Override
	public String toString() {
		return getPath();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Volume) {
			Volume other = (Volume) obj;
			return new EqualsBuilder().append(path, other.getPath()).append(accessMode, other.getAccessMode())
					.isEquals();
		} else
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(path).append(accessMode).toHashCode();
	}
	
	public static class Serializer extends JsonSerializer<Volume> {

		@Override
		public void serialize(Volume volume, JsonGenerator jsonGen,
				SerializerProvider serProvider) throws IOException,
				JsonProcessingException {

			jsonGen.writeStartObject();
			jsonGen.writeFieldName(volume.getPath());
			jsonGen.writeString(Boolean.toString(volume.getAccessMode().equals(AccessMode.rw) ? true: false));
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
