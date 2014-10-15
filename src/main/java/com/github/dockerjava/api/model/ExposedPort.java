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
import com.github.dockerjava.api.model.Ports.Binding;

/**
 * Represents a container port that Docker exposes to external clients.
 * The port is defined by its {@link #getPort() port number} and a 
 * {@link #getScheme() scheme}, e.g. <code>tcp</code>.
 * It can be published by Docker by {@link Ports#bind(ExposedPort, Binding) binding}
 * it to a host port, represented by a {@link Binding}.
 */
@JsonDeserialize(using = ExposedPort.Deserializer.class)
@JsonSerialize(using = ExposedPort.Serializer.class)
public class ExposedPort {

	private final String scheme;

	private final int port;

	/**
	 * Creates an {@link ExposedPort} for the given parameters.
	 * 
	 * @param scheme the {@link #getScheme() scheme}, <code>tcp</code> or 
	 *        <code>udp</code>
	 * @param port the {@link #getPort() port number}
	 */
	public ExposedPort(String scheme, int port) {
		this.scheme = scheme;
		this.port = port;
	}

	/**
	 * @return the scheme (IP protocol), <code>tcp</code> or <code>udp</code>
	 */
	public String getScheme() {
		return scheme;
	}

	public int getPort() {
		return port;
	}

	/**
	 * Creates an {@link ExposedPort} for the TCP scheme.
	 * This is a shortcut for <code>new ExposedPort("tcp", port)</code>  
	 */
	public static ExposedPort tcp(int port) {
		return new ExposedPort("tcp", port);
	}

	/**
	 * Creates an {@link ExposedPort} for the UDP scheme.
	 * This is a shortcut for <code>new ExposedPort("udp", port)</code>  
	 */
	public static ExposedPort udp(int port) {
		return new ExposedPort("udp", port);
	}

	/**
	 * Parses a textual port specification (as used by the Docker CLI) to an 
	 * {@link ExposedPort}.
	 * 
	 * @param serialized the specification, e.g. <code>80/tcp</code>
	 * @return an {@link ExposedPort} matching the specification
	 * @throws IllegalArgumentException if the specification cannot be parsed
	 */
	public static ExposedPort parse(String serialized) throws IllegalArgumentException {
		try {
			String[] parts = serialized.split("/");
			ExposedPort out = new ExposedPort(parts[1], Integer.valueOf(parts[0]));
			return out;
		} catch (Exception e) {
			throw new IllegalArgumentException("Error parsing ExposedPort '" + serialized + "'");
		}
	}
	
	/**
	 * Returns a string representation of this {@link ExposedPort} suitable
	 * for inclusion in a JSON message.
	 * The format is <code>port/scheme</code>, like the argument in {@link #parse(String)}.
	 * 
	 * @return a string representation of this {@link ExposedPort}
	 */
	@Override
	public String toString() {
		return port + "/" + scheme;
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
