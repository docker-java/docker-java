package com.github.dockerjava.client.model;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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


@JsonDeserialize(using=Ports.Deserializer.class)
@JsonSerialize(using=Ports.Serializer.class)
public class Ports {


    private final Map<String, Port> ports = new HashMap<String, Port>();

    public Ports() { }

    public void addPort(Port port) {
        ports.put(port.getPort(), port);
    }
    
    public void addPort(String full, String hostIp, String hostPort) {
        addPort(makePort(full, hostIp, hostPort));
    }

    @Override
    public String toString(){
        return ports.toString();
    }

    public Collection<Port> getAllPorts(){
        return ports.values();
    }
    
    public static Port makePort(String full, String hostIp, String hostPort) {
        if (full == null) return null;
        String[] pieces = full.split("/");
        return new Port(pieces[1], pieces[0], hostIp, hostPort);
    }


    public static class Port{

        private final String scheme;
        private final String port;
        private final String hostIp;
        private final String hostPort;

        public Port(String scheme_, String port_, String hostIp_, String hostPort_) {
            scheme = scheme_;
            port = port_;
            hostIp = hostIp_;
            hostPort = hostPort_;
        }

        public String getScheme() {
            return scheme;
        }

        public String getPort() {
            return port;
        }

        public String getHostIp() {
            return hostIp;
        }

        public String getHostPort() {
            return hostPort;
        }

        
        @Override
        public String toString() {
            return "Port{" +
                    "scheme='" + scheme + '\'' +
                    ", port='" + port + '\'' +
                    ", hostIp='" + hostIp + '\'' +
                    ", hostPort='" + hostPort + '\'' +
                    '}';
        }
        
        @Override
        public boolean equals(Object obj) {
        	if(obj instanceof Port) {
        		Port other = (Port) obj;
        		return new EqualsBuilder().append(scheme, other.getScheme())
        			.append(port, other.getPort())
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

                Map.Entry<String, JsonNode> field = it.next();
                if (!field.getValue().equals(NullNode.getInstance())) {
                    String hostIp = field.getValue().get(0).get("HostIp").textValue();
                    String hostPort = field.getValue().get(0).get("HostPort").textValue();
                    out.addPort(makePort(field.getKey(), hostIp, hostPort));
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
            for(Port p : portBindings.getAllPorts()){
                jsonGen.writeFieldName(p.getPort() + "/" + p.getScheme());
                jsonGen.writeStartArray();
                jsonGen.writeStartObject();
                jsonGen.writeStringField("HostIp", p.hostIp);
                jsonGen.writeStringField("HostPort", p.hostPort);
                jsonGen.writeEndObject();
                jsonGen.writeEndArray();
            }
            jsonGen.writeEndObject();
        }

    }

}
