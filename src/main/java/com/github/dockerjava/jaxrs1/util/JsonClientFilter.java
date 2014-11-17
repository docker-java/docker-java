package com.github.dockerjava.jaxrs1.util;


import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class JsonClientFilter extends ClientFilter {

    @Override
    public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
        // Call the next filter
        ClientResponse resp = getNext().handle(cr);
        String respContentType = resp.getHeaders().getFirst("Content-Type");
        if (respContentType != null && respContentType.startsWith("text/plain")) {
            String newContentType = "application/json" + respContentType.substring(10);
            resp.getHeaders().putSingle("Content-Type", newContentType);
        }
        return resp;
    }
}
