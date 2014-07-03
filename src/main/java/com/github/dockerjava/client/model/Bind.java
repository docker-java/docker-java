package com.github.dockerjava.client.model;

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

//@JsonDeserialize(using = Bind.Deserializer.class)
//@JsonSerialize(using = Bind.Serializer.class)
public class Bind {

	private String path;
	
	private Volume volume;
	
	private boolean readOnly = false;

	public Bind(String path, Volume volume) {
		this(path, volume, false);
	}
	
	public Bind(String path, Volume volume, boolean readOnly) {
		this.path = path;
		this.volume = volume;
		this.readOnly = readOnly;
	}

	public String getPath() {
		return path;
	}

	public Volume getVolume() {
		return volume;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public static Bind parse(String serialized) {
		try {
			String[] parts = serialized.split(":");
			switch(parts.length) {
			 case 2: {
				 return new Bind(parts[0], Volume.parse(parts[1]));
			 }
			 case 3: {
				 if("rw".equals(parts[3].toLowerCase()))
					 return new Bind(parts[0], Volume.parse(parts[1]), true);
				 else throw new RuntimeException("Error parsing Bind '" + serialized + "'");
			 }
			 default: {
				 throw new RuntimeException("Error parsing Bind '" + serialized + "'");
			 }
			}
		} catch (Exception e) {
			throw new RuntimeException("Error parsing Bind '" + serialized + "'");
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Bind) {
			Bind other = (Bind) obj;
			return new EqualsBuilder().append(path, other.getPath()).append(volume, other.getVolume()).append(readOnly, other.isReadOnly())
					.isEquals();
		} else
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(path).append(volume).append(readOnly).toHashCode();
	}
	
	public static class Serializer extends JsonSerializer<Bind> {

		@Override
		public void serialize(Bind bind, JsonGenerator jsonGen,
				SerializerProvider serProvider) throws IOException,
				JsonProcessingException {

			
			
			//jsonGen.writeStartObject();
			//jsonGen.writeFieldName(s);
//			jsonGen.writeStartObject();
//			jsonGen.writeEndObject();
//			jsonGen.writeEndObject();
		}

	}

	public static class Deserializer extends JsonDeserializer<Bind> {
		@Override
		public Bind deserialize(JsonParser jsonParser,
				DeserializationContext deserializationContext)
				throws IOException, JsonProcessingException {
			ObjectCodec oc = jsonParser.getCodec();
			JsonNode node = oc.readTree(jsonParser);
			if (!node.equals(NullNode.getInstance())) {
				Entry<String, JsonNode> field = node.fields().next();
				return Bind.parse(field.getKey());
			} else {
				return null;
			}
		}
	}

	

}
