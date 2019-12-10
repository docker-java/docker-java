package com.github.dockerjava.jaxrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.core.InvocationBuilder;
import com.github.dockerjava.core.WebTarget;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

class JerseyWebTarget implements WebTarget {

    private static final String PATH_SEPARATOR = "/";

    private final ObjectMapper objectMapper;

    private final javax.ws.rs.client.WebTarget webTarget;

    JerseyWebTarget(ObjectMapper objectMapper, javax.ws.rs.client.WebTarget webTarget) {
        this.objectMapper = objectMapper;
        this.webTarget = webTarget;
    }

    @Override
    public InvocationBuilder request() {
        return new JerseyInvocationBuilder(objectMapper, webTarget.request());
    }

    @Override
    public JerseyWebTarget path(String... components) {
        return new JerseyWebTarget(
                objectMapper,
                webTarget.path(String.join(PATH_SEPARATOR, components))
        );
    }

    @Override
    public JerseyWebTarget resolveTemplate(String name, Object value) {
        return new JerseyWebTarget(
                objectMapper,
                webTarget.resolveTemplate(name, value)
        );
    }

    @Override
    public JerseyWebTarget queryParam(String name, Object value) {
        return new JerseyWebTarget(
                objectMapper,
                webTarget.queryParam(name, value)
        );
    }

    @Override
    public JerseyWebTarget queryParamsSet(String name, Set<?> values) {
        return new JerseyWebTarget(
                objectMapper,
                webTarget.queryParam(name, values.toArray())
        );
    }

    @Override
    public JerseyWebTarget queryParamsJsonMap(String name, Map<String, String> values) {
        if (values != null && !values.isEmpty()) {
            try {
                // when param value is JSON string
                return queryParam(name, objectMapper.writeValueAsString(values));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return this;
        }
    }
}
