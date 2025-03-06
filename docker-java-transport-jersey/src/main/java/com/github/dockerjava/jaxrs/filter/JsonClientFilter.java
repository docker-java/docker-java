package com.github.dockerjava.jaxrs.filter;

import java.io.IOException;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.core.MediaType;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@Deprecated
public class JsonClientFilter implements ClientResponseFilter {

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        if (responseContext.getMediaType() != null
                && responseContext.getMediaType().isCompatible(MediaType.TEXT_PLAIN_TYPE)) {
            String newContentType = "application/json" + responseContext.getMediaType().toString().substring(10);
            responseContext.getHeaders().putSingle("Content-Type", newContentType);
        }
    }
}
