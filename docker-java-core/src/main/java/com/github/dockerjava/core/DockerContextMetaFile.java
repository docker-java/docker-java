package com.github.dockerjava.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
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

    public static DockerContextMetaFile loadContextMetaOrNull(ObjectMapper objectMapper, File dockerContextMetaFile) {
        try {
            loadContextMeta(objectMapper, dockerContextMetaFile)
        } catch (Exception exception) {
            return null;
        }
    }

    public static Stream<DockerContextMetaFile> loadAllContextMetaFiles(ObjectMapper objectMapper, File dockerConfigPath) throws IOException {
        final File contextPath = new File(dockerConfigPath, "contexts/meta");
        File[] files = contextPath.listFiles();
        if (files == null) {
            return Stream.of();
        }
        return Arrays.stream(files)
            .map(dir -> new File(dir, "meta.json"))
            .map(file -> loadContextMetaOrNull(objectMapper, file))
            .filter(Objects::nonNull);
    }

    public static void findContextMetaFile(String context) {
        // TODO
    }
    
    public static void main(String[] args) {
        // TODO: We should support the DOCKER_CONTEXT env var as per https://docs.docker.com/engine/context/working-with-contexts/
        loadAllContextMetaFiles(new File("/Users/simon/.docker"));

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
