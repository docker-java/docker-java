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

    @JsonProperty("Storage")
    Storage storage;

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

    public static class Storage {

        @JsonProperty("TLSPath")
        String tlsPath;
        @JsonProperty("MetadataPath")
        String metadataPath;
    }

    public static Optional<DockerContextMetaFile> resolveContextMetaFile(ObjectMapper objectMapper, File dockerConfigPath, String context) {
        final File path = dockerConfigPath.toPath()
            .resolve("contexts")
            .resolve("meta")
            .resolve(metaHashFunction.hashString(context, StandardCharsets.UTF_8).toString())
            .resolve("meta.json")
            .toFile();
        return Optional.ofNullable(loadContextMetaFile(objectMapper, path));
    }

    public static DockerContextMetaFile loadContextMetaFile(ObjectMapper objectMapper, File dockerContextMetaFile) {
        try {
            return parseContextMetaFile(objectMapper, dockerContextMetaFile);
        } catch (Exception exception) {
            return null;
        }
    }

    public static DockerContextMetaFile parseContextMetaFile(ObjectMapper objectMapper, File dockerContextMetaFile) throws IOException {
        try {
            return objectMapper.readValue(dockerContextMetaFile, DockerContextMetaFile.class);
        } catch (IOException e) {
            throw new IOException("Failed to parse docker context meta file " + dockerContextMetaFile, e);
        }
    }
}
