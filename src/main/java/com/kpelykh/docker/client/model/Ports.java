package com.kpelykh.docker.client.model;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ben on 16/12/13.
 */
@JsonDeserialize(using=Ports.Deserializer.class)
public class Ports {
    private final Map<String, Port> ports = new HashMap<>();
    private Port[] mapping;

    private Ports() { }

    private void addPort(Port port) {
        ports.put(port.getPort(), port);
    }

    private void addMapping(Port src, Port target) {
        Port p = ports.get(src);
        if (p==null) return;
//        p.addMapping(target);
    }

    public static class Port{
        private final String scheme;
        private final String port;

        public Port(String scheme_, String port_) {
            scheme = scheme_;
            port = port_;
        }

        public String getScheme() {
            return scheme;
        }

        public String getPort() {
            return port;
        }

        public static Port makePort(String full) {
            if (full == null) return null;
            String[] pieces = full.split("/");
            return new Port(pieces[1], pieces[0]);
        }

        @Override
        public String toString() {
            return "Port{" +
                    "scheme='" + scheme + '\'' +
                    ", port='" + port + '\'' +
                    '}';
        }
    }

    public static class Deserializer extends JsonDeserializer<Ports> {
        @Override
        public Ports deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            Ports out = new Ports();
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);
            for (Iterator<String> it = node.getFieldNames(); it.hasNext();) {
                String pname = it.next();
                out.addPort(Port.makePort(pname));

            }
            return out;
        }
    }

}
