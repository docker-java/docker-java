package com.github.dockerjava.api.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

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
import com.github.dockerjava.api.command.InspectContainerResponse.HostConfig;
import com.github.dockerjava.api.command.InspectContainerResponse.NetworkSettings;

/**
 * A container for port bindings, made available as a {@link Map} via its
 * {@link #getBindings()} method.
 * 
 * @see HostConfig#getPortBindings()
 * @see NetworkSettings#getPorts()
 */
@JsonDeserialize(using = Ports.Deserializer.class)
@JsonSerialize(using = Ports.Serializer.class)
public class Ports {

    private final Map<ExposedPort, Binding[]> ports = new HashMap<ExposedPort, Binding[]>();

    public Ports() { }

    public Ports(ExposedPort exposedPort, Binding host) {
    	bind(exposedPort, host);
    }

    public void bind(ExposedPort exposedPort, Binding host) {
        if (ports.containsKey(exposedPort)) {
            Binding[] bindings = ports.get(exposedPort);
            ports.put(exposedPort, (Binding[]) ArrayUtils.add(bindings, host));
        } else {
            ports.put(exposedPort, new Binding[]{host});
        }
    }

    @Override
    public String toString(){
        return ports.toString();
    }

    /**
     * @return the port bindings as a {@link Map} that contains one or more
     *         {@link Binding}s per {@link ExposedPort}.
     */
    public Map<ExposedPort, Binding[]> getBindings(){
        return ports;
    }

    public static Binding Binding(String hostIp, int hostPort) {
    	return new Binding(hostIp, hostPort);
    }
    public static Binding Binding(int hostPort) {
    	return new Binding(hostPort);
    }


    /**
     * The host part of a port binding.
     * In a port binding a container port, expressed as an {@link ExposedPort},
     * is published as a port of the Docker host.
     * 
     * @see ExposedPort
     */
    public static class Binding {

        private final String hostIp;

        private final int hostPort;

        /**
         * Creates the host part of a port binding.
         * 
         * @see Ports#bind(ExposedPort, Binding)
         * @see ExposedPort
         */
        public Binding(String hostIp, int hostPort) {
            this.hostIp = hostIp;
            this.hostPort = hostPort;
        }

        public Binding(int hostPort) {
            this("", hostPort);
        }

        public String getHostIp() {
            return hostIp;
        }

        public int getHostPort() {
            return hostPort;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
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
                    jsonGen.writeStringField("HostIp", binding.getHostIp());
                    jsonGen.writeStringField("HostPort", "" + binding.getHostPort());
                    jsonGen.writeEndObject();
                }
                jsonGen.writeEndArray();
            }
            jsonGen.writeEndObject();
        }

    }

}
