package com.github.dockerjava.jaxrs.jersey.jaxrs.jersey;

import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.jaxrs.jersey.jaxrs.BuildImageCmdExec;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;

import javax.ws.rs.client.WebTarget;

/**
 * Created by cruffalo on 11/2/15.
 */
public class JerseyBuildImageCmdExec extends BuildImageCmdExec {

    public JerseyBuildImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected void detailImplementation(WebTarget webTarget) {
        webTarget.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.CHUNKED);
        webTarget.property(ClientProperties.CHUNKED_ENCODING_SIZE, 1024 * 1024);
    }
}
