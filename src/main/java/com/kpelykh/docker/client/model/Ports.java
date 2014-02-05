package com.kpelykh.docker.client.model;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ben on 16/12/13.
 */
@JsonDeserialize(using=Ports.Deserializer.class)
@JsonSerialize(using=Ports.Serializer.class)
public class Ports {


    private final Map<String, Port> ports = new HashMap<String, Port>();

    public Ports() { }

    public void addPort(Port port) {
        ports.put(port.getPort(), port);
    }

    @Override
    public String toString(){
        return ports.toString();
    }

    public Map<String, Port> getAllPorts(){
        return ports;
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

        public static Port makePort(String full, String hostIp, String hostPort) {
            if (full == null) return null;
            String[] pieces = full.split("/");
            return new Port(pieces[1], pieces[0], hostIp, hostPort);
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
    }

    public static class Deserializer extends JsonDeserializer<Ports> {
        @Override
        public Ports deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

            Ports out = new Ports();
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);
            for (Iterator<Map.Entry<String, JsonNode>> it = node.getFields(); it.hasNext();) {

                Map.Entry<String, JsonNode> field = it.next();
                String hostIp = field.getValue().get(0).get("HostIp").getTextValue();
                String hostPort = field.getValue().get(0).get("HostPort").getTextValue();
                out.addPort(Port.makePort(field.getKey(), hostIp, hostPort));

            }
            return out;
        }
    }

    public static class Serializer extends JsonSerializer<Ports> {

        @Override
        public void serialize(Ports ports, JsonGenerator jsonGen,
                              SerializerProvider serProvider) throws IOException, JsonProcessingException {

            jsonGen.writeStartObject();//{
            for(String portKey : ports.getAllPorts().keySet()){
                Port p = ports.getAllPorts().get(portKey);
                jsonGen.writeFieldName(p.getPort() + "/" + p.getScheme());
                jsonGen.writeStartArray();
                jsonGen.writeStartObject();
                jsonGen.writeStringField("HostIp", p.hostIp);
                jsonGen.writeStringField("HostPort", p.hostPort);
                jsonGen.writeEndObject();
                jsonGen.writeEndArray();
            }
            jsonGen.writeEndObject();//}
        }

    }

}