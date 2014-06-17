package com.github.dockerjava.client.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
            JsonNode portNodes = oc.readTree(jsonParser);
            if (portNodes.isArray()) {
                for (JsonNode portNode : portNodes) {
                    String hostIp = portNode.get("IP").textValue();
                    String hostPort = portNode.get("PublicPort").asText();
                    String privatePort = portNode.get("PrivatePort").asText();
                    String scheme = portNode.get("Type").textValue();
                    out.addPort(new Port(scheme, privatePort, hostIp, hostPort));
                }
            }
            return out;
        }
    }

    public static class Serializer extends JsonSerializer<Ports> {

        @Override
        public void serialize(Ports ports, JsonGenerator jsonGen,
                              SerializerProvider serProvider) throws IOException, JsonProcessingException {

            jsonGen.writeStartArray();
            for(String portKey : ports.getAllPorts().keySet()){
                Port p = ports.getAllPorts().get(portKey);
                jsonGen.writeStartObject();
                jsonGen.writeStringField("IP", p.hostIp);
                jsonGen.writeNumberField("PrivatePort", Integer.parseInt(p.getPort()));
                jsonGen.writeNumberField("PublicPort", Integer.parseInt(p.getHostPort()));
                jsonGen.writeStringField("Type", p.getScheme());
                jsonGen.writeEndObject();
            }
            jsonGen.writeEndArray();
        }

    }

}