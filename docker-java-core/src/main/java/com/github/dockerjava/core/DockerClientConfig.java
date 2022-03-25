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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.DockerObject;
import com.github.dockerjava.api.model.DockerObjectAccessor;
import com.github.dockerjava.core.exec.AbstrDockerCmdExec;
import com.google.common.io.BaseEncoding;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

import static com.github.dockerjava.core.RemoteApiVersion.UNKNOWN_VERSION;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_19;

/**
 * Interface that describes the docker client configuration.
 *
 * @author Marcus Linke
 *
 */
public interface DockerClientConfig {

    static ObjectMapper getDefaultObjectMapper() {
        return DefaultObjectMapperHolder.INSTANCE.getObjectMapper().copy();
    }

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
        return getDefaultObjectMapper();
    }

    @Nonnull
    default String registryConfigs(@Nonnull AuthConfigurations authConfigs, AbstrDockerCmdExec abstrDockerCmdExec) {
        try {
            final String json;
            final RemoteApiVersion apiVersion = getApiVersion();
            ObjectMapper objectMapper = getObjectMapper();

            if (apiVersion.equals(UNKNOWN_VERSION)) {
                ObjectNode rootNode = objectMapper.valueToTree(authConfigs.getConfigs()); // all registries
                final ObjectNode authNodes = objectMapper.valueToTree(authConfigs); // wrapped in "configs":{}
                rootNode.setAll(authNodes); // merge 2 variants
                json = rootNode.toString();
            } else if (apiVersion.isGreaterOrEqual(VERSION_1_19)) {
                json = objectMapper.writeValueAsString(authConfigs.getConfigs());
            } else {
                json = objectMapper.writeValueAsString(authConfigs);
            }
            return BaseEncoding.base64Url().encode(json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
