package com.github.dockerjava.client;

import java.io.IOException;
import java.util.Set;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableSet;

import org.glassfish.jersey.filter.LoggingFilter;

/**
 * A version of the logging filter that will avoid trying to log entities which can cause
 * issues with the console.
 * 
 * @author sfitts
 *
 */
public class SelectiveLoggingFilter extends LoggingFilter {
    
    private static final Set<String> SKIPPED_CONTENT = ImmutableSet.<String>builder()
            .add(MediaType.APPLICATION_OCTET_STREAM)
            .add("application/tar")
            .build();

    @Override
    public void filter(ClientRequestContext context) throws IOException {
        // Unless the content type is in the list of those we want to ellide, then just have
        // our super-class handle things.
        Object contentType = context.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        if (contentType == null || !SKIPPED_CONTENT.contains(contentType.toString())) {
            super.filter(context);
        }
    }
    
}
