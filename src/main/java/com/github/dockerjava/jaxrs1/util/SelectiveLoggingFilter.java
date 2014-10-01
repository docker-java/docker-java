package com.github.dockerjava.jaxrs1.util;

import java.util.Set;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableSet;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;

/**
 * A version of the logging filter that will avoid trying to log entities which can cause
 * issues with the console.
 * 
 * @author sfitts
 *
 */
public class SelectiveLoggingFilter extends com.sun.jersey.api.client.filter.LoggingFilter {
    
    
    private static final Set<String> SKIPPED_CONTENT = ImmutableSet.<String>builder()
            .add(MediaType.APPLICATION_OCTET_STREAM)
            .add("application/tar")
            .build();

    @Override
    public ClientResponse handle(ClientRequest request) throws ClientHandlerException {
        // Unless the content type is in the list of those we want to ellide, then just have
        // our super-class handle things.
        Object contentType = request.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        if (contentType != null && SKIPPED_CONTENT.contains(contentType.toString())) {
            // Skip logging this.
            //
            // N.B. -- I'd actually love to reproduce (or better yet just use) the logging code from
            // our super-class.  However, everything is private (so we can't use it) and the code
            // is under a modified GPL which means we can't pull it into an ASL project.  Right now
            // I don't have the energy to do a clean implementation.
            return getNext().handle(request);
        }

        // Do what we normally would
        return super.handle(request);
    }
    
}
