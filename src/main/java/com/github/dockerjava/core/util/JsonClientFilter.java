package com.github.dockerjava.core.util;


import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class JsonClientFilter implements ClientResponseFilter {


    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        if (responseContext.getMediaType() != null && responseContext.getMediaType().isCompatible(MediaType.TEXT_PLAIN_TYPE)) {
            String newContentType = "application/json" + responseContext.getMediaType().toString().substring(10);
            responseContext.getHeaders().putSingle("Content-Type", newContentType);
        }
    }
}
