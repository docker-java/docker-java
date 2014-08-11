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

@JsonDeserialize(using = ExposedPort.Deserializer.class)
@JsonSerialize(using = ExposedPort.Serializer.class)
public class ExposedPort {

	private String scheme;

	private int port;

	public ExposedPort(String scheme, int port) {
		this.scheme = scheme;
		this.port = port;
	}

	public String getScheme() {
		return scheme;
	}

	public int getPort() {
		return port;
	}

	public static ExposedPort tcp(int port) {
		return new ExposedPort("tcp", port);
	}

	public static ExposedPort parse(String serialized) {
		try {
			String[] parts = serialized.split("/");
			ExposedPort out = new ExposedPort(parts[1], Integer.valueOf(parts[0]));
			return out;
		} catch (Exception e) {
			throw new RuntimeException("Error parsing ExposedPort '" + serialized + "'");
		}
	}
	
	@Override
	public String toString() {
		return getPort() + "/" + getScheme();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ExposedPort) {
			ExposedPort other = (ExposedPort) obj;
			return new EqualsBuilder().append(scheme, other.getScheme())
					.append(port, other.getPort()).isEquals();
		} else
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(scheme).append(port).toHashCode();
	}

	public static class Deserializer extends JsonDeserializer<ExposedPort> {
		@Override
		public ExposedPort deserialize(JsonParser jsonParser,
				DeserializationContext deserializationContext)
				throws IOException, JsonProcessingException {
			ObjectCodec oc = jsonParser.getCodec();
			JsonNode node = oc.readTree(jsonParser);
			if (!node.equals(NullNode.getInstance())) {
				Entry<String, JsonNode> field = node.fields().next();
				return ExposedPort.parse(field.getKey());
			} else {
				return null;
			}
		}
	}

	public static class Serializer extends JsonSerializer<ExposedPort> {

		@Override
		public void serialize(ExposedPort exposedPort, JsonGenerator jsonGen,
				SerializerProvider serProvider) throws IOException,
				JsonProcessingException {

			jsonGen.writeStartObject();
			jsonGen.writeFieldName(exposedPort.toString());
			jsonGen.writeEndObject();
		}

	}

}
