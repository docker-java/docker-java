package com.github.dockerjava.client.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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

@JsonDeserialize(using = Ports.Deserializer.class)
@JsonSerialize(using = Ports.Serializer.class)
public class Ports {

    private final Map<ExposedPort, Binding> ports = new HashMap<ExposedPort, Binding>();

    public Ports() { }
    
    public Ports(ExposedPort exposedPort, Binding host) { 
    	bind(exposedPort, host);
    }

    public void bind(ExposedPort exposedPort, Binding host) {
    	ports.put(exposedPort, host);
    }

    @Override
    public String toString(){
        return ports.toString();
    }

    public Map<ExposedPort, Binding> getBindings(){
        return ports;
    }
    
    public static Binding Binding(String hostIp, int hostPort) {
    	return new Binding(hostIp, hostPort);
    }
    public static Binding Binding(int hostPort) {
    	return new Binding(hostPort);
    }


    public static class Binding {


        private final String hostIp;

        private final int hostPort;

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
            return "PortBinding{" +
                    "hostIp='" + hostIp + '\'' +
                    ", hostPort='" + hostPort + '\'' +
                    '}';
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

                Map.Entry<String, JsonNode> field = it.next();
                if (!field.getValue().equals(NullNode.getInstance())) {                	
                    String hostIp = field.getValue().get(0).get("HostIp").textValue();
                    int hostPort = field.getValue().get(0).get("HostPort").asInt();
                    out.bind(ExposedPort.parse(field.getKey()), new Binding(hostIp, hostPort));
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
            for(Entry<ExposedPort, Binding> entry : portBindings.getBindings().entrySet()){
                jsonGen.writeFieldName(entry.getKey().getPort() + "/" + entry.getKey().getScheme());
                jsonGen.writeStartArray();
                jsonGen.writeStartObject();
                jsonGen.writeStringField("HostIp", entry.getValue().getHostIp());
                jsonGen.writeStringField("HostPort", "" + entry.getValue().getHostPort());
                jsonGen.writeEndObject();
                jsonGen.writeEndArray();
            }
            jsonGen.writeEndObject();
        }

    }

}
