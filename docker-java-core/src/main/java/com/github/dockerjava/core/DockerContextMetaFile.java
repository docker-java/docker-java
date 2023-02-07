package com.github.dockerjava.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class DockerContextMetaFile {
    private static HashFunction metaHashFunction = Hashing.sha256();

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

    public static Optional<DockerContextMetaFile> resolveContextMetaFile(ObjectMapper objectMapper, File dockerConfigPath, String context) {
        final File path = dockerConfigPath.toPath()
            .resolve("contexts")
            .resolve("meta")
            .resolve(metaHashFunction.hashString(context, StandardCharsets.UTF_8).toString())
            .resolve("meta.json")
            .toFile();
        return Optional.ofNullable(loadContextMeta(objectMapper, path));
    }

    public static DockerContextMetaFile loadContextMeta(ObjectMapper objectMapper, File dockerContextMetaFile) {
        try {
            return parseContextMeta(objectMapper, dockerContextMetaFile);
        } catch (Exception exception) {
            return null;
        }
    }

    public static DockerContextMetaFile parseContextMeta(ObjectMapper objectMapper, File dockerContextMetaFile) throws IOException {
        try {
            return objectMapper.readValue(dockerContextMetaFile, DockerContextMetaFile.class);
        } catch (IOException e) {
            throw new IOException("Failed to parse docker context meta file " + dockerContextMetaFile, e);
        }
    }
}
