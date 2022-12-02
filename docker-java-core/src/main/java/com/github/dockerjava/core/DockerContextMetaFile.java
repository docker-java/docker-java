package com.github.dockerjava.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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

    public Optional<String> host() {
        if (endpoints != null && endpoints.docker != null) {
            return Optional.ofNullable(endpoints.docker.host);
        }
        return Optional.empty();
    }

    public static Optional<DockerContextMetaFile> findContextMetaFile(ObjectMapper objectMapper, File dockerConfigPath, String context) {
        return loadAllContextMetaFiles(objectMapper, dockerConfigPath)
            .filter(metaFile -> metaFile.endpoints != null && metaFile.endpoints.docker != null)
            .filter(metaFile -> Objects.equals(metaFile.name, context))
            .findFirst();
    }

    public static Stream<DockerContextMetaFile> loadAllContextMetaFiles(ObjectMapper objectMapper, File dockerConfigPath) {
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

    public static DockerContextMetaFile loadContextMetaOrNull(ObjectMapper objectMapper, File dockerContextMetaFile) {
        try {
            return loadContextMeta(objectMapper, dockerContextMetaFile);
        } catch (Exception exception) {
            return null;
        }
    }

    public static DockerContextMetaFile loadContextMeta(ObjectMapper objectMapper, File dockerContextMetaFile) throws IOException {
        try {
            return objectMapper.readValue(dockerContextMetaFile, DockerContextMetaFile.class);
        } catch (IOException e) {
            throw new IOException("Failed to parse docker context meta file " + dockerContextMetaFile, e);
        }
    }

    public static void main(String[] args) {
        ObjectMapper mapper = DefaultObjectMapperHolder.INSTANCE.getObjectMapper().copy();
        String host = findContextMetaFile(mapper, new File("/Users/simon/.docker"), "colima")
            .map(file -> file.endpoints.docker.host)
            .orElse(null);

        System.out.printf("Host is %s\n", host);
    }
}
