package com.github.dockerjava.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.transport.DockerHttpClient;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class DefaultDockerCmdExecFactory extends AbstractDockerCmdExecFactory {

    private final DockerHttpClient dockerHttpClient;

    private final ObjectMapper objectMapper;

    public DefaultDockerCmdExecFactory(
        DockerHttpClient dockerHttpClient,
        ObjectMapper objectMapper
    ) {
        this.dockerHttpClient = dockerHttpClient;
        this.objectMapper = objectMapper;
    }

    public DockerHttpClient getDockerHttpClient() {
        return dockerHttpClient;
    }

    @Override
    protected WebTarget getBaseResource() {
        return new DefaultWebTarget();
    }

    @Override
    public void close() throws IOException {
        dockerHttpClient.close();
    }

    private class DefaultWebTarget implements WebTarget {

        final ImmutableList<String> path;

        final SetMultimap<String, String> queryParams;

        DefaultWebTarget() {
            this(
                ImmutableList.of(),
                MultimapBuilder.hashKeys().hashSetValues().build()
            );
        }

        DefaultWebTarget(
            ImmutableList<String> path,
            SetMultimap<String, String> queryParams
        ) {
            this.path = path;
            this.queryParams = queryParams;
        }

        @Override
        public String toString() {
            return String.format("DefaultWebTarget{path=%s, queryParams=%s}", path, queryParams);
        }

        @Override
        public InvocationBuilder request() {
            String resource = StringUtils.join(path, "/");

            if (!resource.startsWith("/")) {
                resource = "/" + resource;
            }

            if (!queryParams.isEmpty()) {
                Escaper urlFormParameterEscaper = UrlEscapers.urlFormParameterEscaper();
                resource = queryParams.asMap().entrySet().stream()
                    .flatMap(entry -> {
                        return entry.getValue().stream().map(s -> {
                            return entry.getKey() + "=" + urlFormParameterEscaper.escape(s);
                        });
                    })
                    .collect(Collectors.joining("&", resource + "?", ""));
            }

            return new DefaultInvocationBuilder(
                dockerHttpClient, objectMapper, resource
            );
        }

        @Override
        public DefaultWebTarget path(String... components) {
            ImmutableList<String> newPath = ImmutableList.<String>builder()
                .addAll(path)
                .add(components)
                .build();
            return new DefaultWebTarget(newPath, queryParams);
        }

        @Override
        public DefaultWebTarget resolveTemplate(String name, Object value) {
            ImmutableList.Builder<String> newPath = ImmutableList.builder();
            for (String component : path) {
                component = component.replaceAll(
                    "\\{" + name + "\\}",
                    UrlEscapers.urlPathSegmentEscaper().escape(value.toString())
                );
                newPath.add(component);
            }

            return new DefaultWebTarget(newPath.build(), queryParams);
        }

        @Override
        public DefaultWebTarget queryParam(String name, Object value) {
            if (value == null) {
                return this;
            }

            SetMultimap<String, String> newQueryParams = HashMultimap.create(queryParams);
            newQueryParams.put(name, value.toString());

            return new DefaultWebTarget(path, newQueryParams);
        }

        @Override
        public DefaultWebTarget queryParamsSet(String name, Set<?> values) {
            SetMultimap<String, String> newQueryParams = HashMultimap.create(queryParams);
            newQueryParams.replaceValues(name, values.stream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.toSet()));

            return new DefaultWebTarget(path, newQueryParams);
        }

        @Override
        public DefaultWebTarget queryParamsJsonMap(String name, Map<String, String> values) {
            if (values == null || values.isEmpty()) {
                return this;
            }

            // when param value is JSON string
            try {
                return queryParam(name, objectMapper.writeValueAsString(values));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
