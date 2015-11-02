package com.github.dockerjava.jaxrs.connector;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.glassfish.jersey.client.ClientRequest;
import org.glassfish.jersey.client.ClientResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import java.io.IOException;

/**
 * Fix for https://github.com/docker-java/docker-java/issues/196
 * 
 * https://java.net/jira/browse/JERSEY-2852
 * 
 * @author marcus
 *
 */
public class ApacheConnectorClientResponse extends ClientResponse {

    private CloseableHttpResponse closeableHttpResponse;

    public ApacheConnectorClientResponse(ClientRequest requestContext, Response response) {
        super(requestContext, response);
    }

    public ApacheConnectorClientResponse(StatusType status, ClientRequest requestContext,
            CloseableHttpResponse closeableHttpResponse) {
        super(status, requestContext);
        this.closeableHttpResponse = closeableHttpResponse;
    }

    public ApacheConnectorClientResponse(StatusType status, ClientRequest requestContext) {
        super(status, requestContext);
    }

    @Override
    public void close() {
        try {
            closeableHttpResponse.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.close();
    }

}
