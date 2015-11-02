package com.github.dockerjava.spi;

import com.github.dockerjava.api.command.DockerCmdExecFactory;

import java.util.ServiceLoader;

/**
 *
 */
public class DockerCmdExecFactoryLoader {

    public static final String DOCKER_CMD_KEY = DockerCmdExecFactory.class.getName();

    public static DockerCmdExecFactory loadCommandFactory() {
        // get system property for override
        final String overrideClassName = System.getProperty(DOCKER_CMD_KEY);
        if(overrideClassName != null && !overrideClassName.isEmpty()) {
            try {
                final Class<?> instance = Class.forName(overrideClassName);
                final Object commandFactory = instance.newInstance();
                if(commandFactory instanceof DockerCmdExecFactory) {
                    return (DockerCmdExecFactory)commandFactory;
                }
                throw new IllegalStateException("An override was specified with the '" + DOCKER_CMD_KEY + "' system property but the '" + DOCKER_CMD_KEY + "' interface was not implemented by the specified class");
            } catch (ClassNotFoundException e) {
                // todo: log warning that specified override class was not found
            } catch (InstantiationException | IllegalAccessException e) {
                // todo: log warning that the class could not be initialized and potentially throw error
            }
        }

        // get service loader
        final ServiceLoader<DockerCmdExecFactory> loader = ServiceLoader.load(DockerCmdExecFactory.class);

        // iterate through found factories
        for(final DockerCmdExecFactory commandFactory : loader) {
            if(commandFactory != null) {
                return commandFactory;
            }
        }

        // throw an error if no instance is found
        throw new IllegalStateException("Could not load an acceptable instance of '" + DOCKER_CMD_KEY + "', please see... "); // todo: document
    }

}
