/*
 * Created on 08.06.2016
 */
package com.github.dockerjava.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.DockerObject;
import com.github.dockerjava.api.model.DockerObjectAccessor;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

/**
 * Interface that describes the docker client configuration.
 *
 * @author Marcus Linke
 *
 */
public interface DockerClientConfig {

    URI getDockerHost();

    RemoteApiVersion getApiVersion();

    String getRegistryUsername();

    String getRegistryPassword();

    String getRegistryEmail();

    String getRegistryUrl();

    AuthConfig effectiveAuthConfig(String imageName);

    AuthConfigurations getAuthConfigurations();

    /**
     * Returns an {@link SSLConfig} when secure connection is configured or null if not.
     */
    SSLConfig getSSLConfig();

    default ObjectMapper getObjectMapper() {
        return DefaultObjectMapperHolder.INSTANCE.getObjectMapper().copy();
    }
}

enum DefaultObjectMapperHolder {
    INSTANCE;

    private final ObjectMapper objectMapper = new ObjectMapper()
            // TODO .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    {
        ObjectMapper originalObjectMapper = objectMapper.copy();
        objectMapper.registerModule(new SimpleModule("docker-java") {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanDeserializerModifier(new BeanDeserializerModifier() {
                    @Override
                    public JsonDeserializer<?> modifyDeserializer(
                        DeserializationConfig config,
                        BeanDescription beanDescription,
                        JsonDeserializer<?> originalDeserializer
                    ) {
                        if (!beanDescription.getType().isTypeOrSubTypeOf(DockerObject.class)) {
                            return originalDeserializer;
                        }

                        return new DockerObjectDeserializer(
                            originalDeserializer,
                            beanDescription,
                            originalObjectMapper
                        );
                    }
                });
            }
        });
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}

class DockerObjectDeserializer extends DelegatingDeserializer {

    private final BeanDescription beanDescription;

    private final ObjectMapper originalMapper;

    DockerObjectDeserializer(
        JsonDeserializer<?> delegate,
        BeanDescription beanDescription,
        ObjectMapper originalMapper
    ) {
        super(delegate);
        this.beanDescription = beanDescription;
        this.originalMapper = originalMapper;
    }

    @Override
    protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> newDelegatee) {
        return new DockerObjectDeserializer(newDelegatee, beanDescription, originalMapper);
    }

    @Override
    @SuppressWarnings({"deprecation", "unchecked"})
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode jsonNode = p.readValueAsTree();

        Object deserializedObject = originalMapper.treeToValue(jsonNode, beanDescription.getBeanClass());

        if (deserializedObject instanceof DockerObject) {
            DockerObjectAccessor.overrideRawValues(
                ((DockerObject) deserializedObject),
                originalMapper.convertValue(jsonNode, HashMap.class)
            );
        }

        return deserializedObject;
    }
}
