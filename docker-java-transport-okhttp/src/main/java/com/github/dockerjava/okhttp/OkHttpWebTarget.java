package com.github.dockerjava.okhttp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.core.InvocationBuilder;
import com.github.dockerjava.core.WebTarget;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.SetMultimap;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class OkHttpWebTarget implements WebTarget {

    final ObjectMapper objectMapper;

    final OkHttpClient okHttpClient;

    final HttpUrl baseUrl;

    final ImmutableList<String> path;

    final SetMultimap<String, String> queryParams;

    OkHttpWebTarget(
            ObjectMapper objectMapper,
            OkHttpClient okHttpClient,
            HttpUrl baseUrl,
            ImmutableList<String> path,
            SetMultimap<String, String> queryParams
    ) {
        this.objectMapper = objectMapper;
        this.okHttpClient = okHttpClient;
        this.baseUrl = baseUrl;
        this.path = path;
        this.queryParams = queryParams;
    }

    @Override
    public InvocationBuilder request() {
        String resource = StringUtils.join(path, "/");

        if (!resource.startsWith("/")) {
            resource = "/" + resource;
        }

        HttpUrl.Builder baseUrlBuilder = baseUrl.newBuilder()
            .encodedPath(resource);

        for (Map.Entry<String, Collection<String>> queryParamEntry : queryParams.asMap().entrySet()) {
            String key = queryParamEntry.getKey();
            for (String paramValue : queryParamEntry.getValue()) {
                baseUrlBuilder.addQueryParameter(key, paramValue);
            }
        }

        return new OkHttpInvocationBuilder(
            objectMapper,
            okHttpClient,
            baseUrlBuilder.build()
        );
    }

    @Override
    public OkHttpWebTarget path(String... components) {
        ImmutableList<String> newPath = ImmutableList.<String>builder()
                .addAll(path)
                .add(components)
                .build();
        return new OkHttpWebTarget(
                objectMapper,
                okHttpClient,
                baseUrl,
                newPath,
                queryParams
        );
    }

    @Override
    public OkHttpWebTarget resolveTemplate(String name, Object value) {
        ImmutableList.Builder<String> newPath = ImmutableList.builder();
        for (String component : path) {
            component = component.replaceAll("\\{" + name + "\\}", value.toString());
            newPath.add(component);
        }

        return new OkHttpWebTarget(
                objectMapper,
                okHttpClient,
                baseUrl,
                newPath.build(),
                queryParams
        );
    }

    @Override
    public OkHttpWebTarget queryParam(String name, Object value) {
        if (value == null) {
            return this;
        }

        SetMultimap<String, String> newQueryParams = HashMultimap.create(queryParams);
        newQueryParams.put(name, value.toString());

        return new OkHttpWebTarget(
                objectMapper,
                okHttpClient,
                baseUrl,
                path,
                newQueryParams
        );
    }

    @Override
    public OkHttpWebTarget queryParamsSet(String name, Set<?> values) {
        SetMultimap<String, String> newQueryParams = HashMultimap.create(queryParams);
        newQueryParams.replaceValues(name, values.stream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.toSet()));

        return new OkHttpWebTarget(
                objectMapper,
                okHttpClient,
                baseUrl,
                path,
                newQueryParams
        );
    }

    @Override
    public OkHttpWebTarget queryParamsJsonMap(String name, Map<String, String> values) {
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
