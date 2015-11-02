package com.github.dockerjava.jaxrs.jersey.jaxrs.spi;

import java.util.ServiceLoader;

/**
 * Created by cruffalo on 11/2/15.
 */
public class ClientBuilderProviderLoader {

    public static final String DOCKER_CLIENT_BUILDER_KEY = ClientBuilderProvider.class.getName();

    public static ClientBuilderProvider loadClientBuilderProvider() {
        // get system property for override
        final String overrideClassName = System.getProperty(DOCKER_CLIENT_BUILDER_KEY);
        if(overrideClassName != null && !overrideClassName.isEmpty()) {
            try {
                final Class<?> instance = Class.forName(overrideClassName);
                final Object clientBuilderProvider = instance.newInstance();
                if(clientBuilderProvider instanceof ClientBuilderProvider) {
                    return (ClientBuilderProvider)clientBuilderProvider;
                }
                throw new IllegalStateException("An override was specified with the '" + DOCKER_CLIENT_BUILDER_KEY + "' system property but the '" + DOCKER_CLIENT_BUILDER_KEY + "' interface was not implemented by the specified class");
            } catch (ClassNotFoundException e) {
                // todo: log warning that specified override class was not found
            } catch (InstantiationException | IllegalAccessException e) {
                // todo: log warning that the class could not be initialized and potentially throw error
            }
        }

        // get service loader
        final ServiceLoader<ClientBuilderProvider> loader = ServiceLoader.load(ClientBuilderProvider.class);

        // iterate through found providers
        for(final ClientBuilderProvider clientBuilderProvider : loader) {
            if(clientBuilderProvider != null) {
                return clientBuilderProvider;
            }
        }

        // return reasonable default instance
        return new DefaultClientBuilderProvider();
    }

}
