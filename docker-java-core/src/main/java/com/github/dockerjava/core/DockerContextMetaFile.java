package com.github.dockerjava.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import javax.annotation.CheckForNull;

public class DockerContextMetaFile {
    @JsonProperty("Name")
    String name;

    @JsonProperty("Endpoints")
    Endpoints endpoints;

    public static class Endpoints {
        @JsonProperty("docker")
        Docker docker;

        public static class Docker {
            @JsonProperty("Host")
            String host;

            @JsonProperty("SkipTLSVerify")
            boolean skipTLSVerify;
        }
    }

    public static DockerContextMetaFile loadContextMeta(ObjectMapper objectMapper, File dockerContextMetaFile) throws IOException {
        try {
            return objectMapper.readValue(dockerContextMetaFile, DockerContextMetaFile.class);
        } catch (IOException e) {
            throw new IOException("Failed to parse docker context meta file " + dockerContextMetaFile, e);
        }
    }

    public static void loadAllContextMetaFiles() {
        // TODO
    }

    public static void findContextMetaFile(String context) {
        // TODO
    }
    
    public static void main(String[] args) {
        ObjectMapper mapper = DefaultObjectMapperHolder.INSTANCE.getObjectMapper().copy();
        try {
            DockerContextMetaFile dockerContextMetaFile = loadContextMeta(mapper,
                new File("/Users/simon/.docker/contexts/meta/f24fd3749c1368328e2b149bec149cb6795619f244c5b584e844961215dadd16/meta.json"));
            System.out.println(dockerContextMetaFile.endpoints.docker.host);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
