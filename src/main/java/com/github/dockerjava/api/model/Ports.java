package com.github.dockerjava.api.model;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

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
import com.github.dockerjava.api.command.InspectContainerResponse.NetworkSettings;

/**
 * A container for port bindings, made available as a {@link Map} via its
 * {@link #getBindings()} method.
 * <p> 
 * <i>Note: This is an abstraction used for querying existing port bindings from 
 * a container configuration.
 * It is not to be confused with the {@link PortBinding} abstraction used for
 * adding new port bindings to a container.</i>
 * 
 * @see HostConfig#getPortBindings()
 * @see NetworkSettings#getPorts()
 */
@JsonDeserialize(using = Ports.Deserializer.class)
@JsonSerialize(using = Ports.Serializer.class)
public class Ports {

    private final Map<ExposedPort, Binding[]> ports = new HashMap<ExposedPort, Binding[]>();

    /**
     * Creates a {@link Ports} object with no {@link PortBinding}s.
     * Use {@link #bind(ExposedPort, Binding)} or {@link #add(PortBinding...)}
     * to add {@link PortBinding}s.
     */
    public Ports() { }

    /**
     * Creates a {@link Ports} object with an initial {@link PortBinding} for 
     * the specified {@link ExposedPort} and {@link Binding}.
     * Use {@link #bind(ExposedPort, Binding)} or {@link #add(PortBinding...)}
     * to add more {@link PortBinding}s.
     */
    public Ports(ExposedPort exposedPort, Binding host) {
    	bind(exposedPort, host);
    }

    /**
     * Adds a new {@link PortBinding} for the specified {@link ExposedPort} and
     * {@link Binding} to the current bindings.
     */
    public void bind(ExposedPort exposedPort, Binding binding) {
        if (ports.containsKey(exposedPort)) {
            Binding[] bindings = ports.get(exposedPort);
            ports.put(exposedPort, (Binding[]) ArrayUtils.add(bindings, binding));
        } else {
            ports.put(exposedPort, new Binding[]{binding});
        }
    }

    /**
     * Adds the specified {@link PortBinding}(s) to the list of {@link PortBinding}s. 
     */
    public void add(PortBinding... portBindings) {
        for (PortBinding binding : portBindings) {
            bind(binding.getExposedPort(), binding.getBinding());
        }
    }

    @Override
    public String toString(){
        return ports.toString();
    }

    /**
     * Returns the port bindings in the format used by the Docker remote API,
     * i.e. the {@link Binding}s grouped by {@link ExposedPort}.
     * 
     * @return the port bindings as a {@link Map} that contains one or more
     *         {@link Binding}s per {@link ExposedPort}.
     */
    public Map<ExposedPort, Binding[]> getBindings(){
        return ports;
    }

    /**
     * Creates a {@link Binding} for the given IP address and port number.
     */
    public static Binding Binding(String hostIp, Integer hostPort) {
    	return new Binding(hostIp, hostPort);
    }

    /**
     * Creates a {@link Binding} for the given port number, leaving the
     * IP address undefined.
     */
    public static Binding Binding(Integer hostPort) {
    	return new Binding(hostPort);
    }


    /**
     * A {@link Binding} represents a socket on the Docker host that is
     * used in a {@link PortBinding}.
     * It is characterized by an {@link #getHostIp() IP address} and a
     * {@link #getHostPort() port number}.
     * Both properties may be <code>null</code> in order to let Docker assign
     * them dynamically/using defaults.
     * 
     * @see Ports#bind(ExposedPort, Binding)
     * @see ExposedPort
     */
    public static class Binding {

        private final String hostIp;

        private final Integer hostPort;

        /**
         * Creates a {@link Binding} for the given {@link #getHostIp() IP address}
         * and {@link #getHostPort() port number}.
         * 
         * @see Ports#bind(ExposedPort, Binding)
         * @see ExposedPort
         */
        public Binding(String hostIp, Integer hostPort) {
            this.hostIp = isEmpty(hostIp) ? null : hostIp;
            this.hostPort = hostPort;
        }

        /**
         * Creates a {@link Binding} for the given {@link #getHostPort() port number},
         * leaving the {@link #getHostIp() IP address} undefined.
         * 
         * @see Ports#bind(ExposedPort, Binding)
         * @see ExposedPort
         */
        public Binding(Integer hostPort) {
            this(null, hostPort);
        }

        /**
         * Creates a {@link Binding} for the given {@link #getHostIp() IP address},
         * leaving the {@link #getHostPort() port number} undefined.
         */
        public Binding(String hostIp) {
            this(hostIp, null);
        }

        /**
         * Creates a {@link Binding} with both {@link #getHostIp() IP address} and
         * {@link #getHostPort() port number} undefined.
         */
        public Binding() {
            this(null, null);
        }

        /**
         * @return the IP address on the Docker host. 
         *         May be <code>null</code>, in which case Docker will bind the
         *         port to all interfaces (<code>0.0.0.0</code>).
         */
        public String getHostIp() {
            return hostIp;
        }

        /**
         * @return the port number on the Docker host.
         *         May be <code>null</code>, in which case Docker will dynamically
         *         assign a port.
         */
        public Integer getHostPort() {
            return hostPort;
        }

        /**
         * Parses a textual host and port specification (as used by the Docker CLI) 
         * to a {@link Binding}.
         * <p>
         * Legal syntax: <code>IP|IP:port|port</code>
         * 
         * @param serialized serialized the specification, e.g. 
         *        <code>127.0.0.1:80</code>
         * @return a {@link Binding} matching the specification
         * @throws IllegalArgumentException if the specification cannot be parsed
         */
        public static Binding parse(String serialized) throws IllegalArgumentException {
            try {
                if (serialized.isEmpty()) {
                    return new Binding();
                }

                String[] parts = serialized.split(":");
                switch (parts.length) {
                case 2: {
                    return new Binding(parts[0], Integer.valueOf(parts[1]));
                }
                case 1: {
                    return parts[0].contains(".") ? new Binding(parts[0]) 
                        : new Binding(Integer.valueOf(parts[0]));
                }
                default: {
                    throw new IllegalArgumentException();
                }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Error parsing Binding '"
                        + serialized + "'");
            }
        }

        /**
         * Returns a string representation of this {@link Binding} suitable
         * for inclusion in a JSON message.
         * The format is <code>[IP:]Port</code>, like the argument in {@link #parse(String)}.
         * 
         * @return a string representation of this {@link Binding}
         */
        @Override
        public String toString() {
            if (isEmpty(hostIp)) {
                return Integer.toString(hostPort);
            } else if (hostPort == null) {
            	return hostIp;
            } else {
                return hostIp + ":" + hostPort;
            }
        }

        @Override
        public boolean equals(Object obj) {
        	if(obj instanceof Binding) {
        		Binding other = (Binding) obj;
        		return new EqualsBuilder()
        			.append(hostIp, other.getHostIp())
        			.append(hostPort, other.getHostPort()).isEquals();
        	} else
        		return super.equals(obj);
        }
    }


    public static class Deserializer extends JsonDeserializer<Ports> {
        @Override
        public Ports deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

            Ports out = new Ports();
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);
            for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext();) {

                Map.Entry<String, JsonNode> portNode = it.next();
                JsonNode bindingsArray = portNode.getValue();
                for (int i = 0; i < bindingsArray.size(); i++) {
                    JsonNode bindingNode = bindingsArray.get(i);
                    if (!bindingNode.equals(NullNode.getInstance())) {
                        String hostIp = bindingNode.get("HostIp").textValue();
                        int hostPort = bindingNode.get("HostPort").asInt();
                        out.bind(ExposedPort.parse(portNode.getKey()), new Binding(hostIp, hostPort));
                    }
                }
            }
            return out;
        }
    }

    public static class Serializer extends JsonSerializer<Ports> {

        @Override
        public void serialize(Ports portBindings, JsonGenerator jsonGen,
                              SerializerProvider serProvider) throws IOException, JsonProcessingException {

            jsonGen.writeStartObject();
            for(Entry<ExposedPort, Binding[]> entry : portBindings.getBindings().entrySet()){
                jsonGen.writeFieldName(entry.getKey().toString());
                jsonGen.writeStartArray();
                for (Binding binding : entry.getValue()) {
                    jsonGen.writeStartObject();
                    jsonGen.writeStringField("HostIp", binding.getHostIp() == null ? "" : binding.getHostIp());
                    jsonGen.writeStringField("HostPort", binding.getHostPort() == null ? "" : binding.getHostPort().toString());
                    jsonGen.writeEndObject();
                }
                jsonGen.writeEndArray();
            }
            jsonGen.writeEndObject();
        }

    }

}
