package com.github.dockerjava.jaxrs.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Response;

/**
 * Default implementation of RedirectStrategy honors the restrictions on automatic redirection of entity enclosing methods such as POST and
 * PUT imposed by the HTTP specification. 302 Moved Temporarily, 301 Moved Permanently and 307 Temporary Redirect status codes will result
 * in an automatic redirect of HEAD and GET methods only.
 *
 * {@link org.apache.http.impl.client.DefaultRedirectStrategy}
 *
 * This filter allows arbitrary redirection for other methods.
 */
public class FollowRedirectsFilter implements ClientResponseFilter {

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        if (!responseContext.getStatusInfo().getFamily().equals(Response.Status.Family.REDIRECTION)) {
            return;
        }

        Response resp = requestContext.getClient().target(responseContext.getLocation()).request()
                .method(requestContext.getMethod());
        responseContext.setEntityStream((InputStream) resp.getEntity());
        responseContext.setStatusInfo(resp.getStatusInfo());
        responseContext.setStatus(resp.getStatus());
    }
}
