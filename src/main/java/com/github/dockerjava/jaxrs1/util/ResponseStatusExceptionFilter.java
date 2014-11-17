package com.github.dockerjava.jaxrs1.util;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;

import com.github.dockerjava.api.BadRequestException;
import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.InternalServerErrorException;
import com.github.dockerjava.api.NotAcceptableException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.UnauthorizedException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

/**
 * This {@link ClientResponseFilter} implementation detects http status codes and throws {@link DockerException}s 
 *
 * @author marcus
 *
 */
public class ResponseStatusExceptionFilter extends ClientFilter {


    @Override
    public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
        // Call the next client handler in the filter chain
        ClientResponse resp = getNext().handle(cr);
        try {
            filter(resp);
        } catch (Exception e) {
            throw new ClientHandlerException(e);
        }
        return resp;
    }

    public void filter(ClientResponse response) throws IOException {
    	int status = response.getStatus();
		switch (status) {
		case 200:
		case 201:	
		case 204:
			return;
		case 304:
			throw new NotModifiedException(getBodyAsMessage(response));
		case 400:
			throw new BadRequestException(getBodyAsMessage(response));
		case 401:
			throw new UnauthorizedException(getBodyAsMessage(response));	
		case 404:
			throw new NotFoundException(getBodyAsMessage(response));
		case 406:
			throw new NotAcceptableException(getBodyAsMessage(response));
		case 409:
			throw new ConflictException(getBodyAsMessage(response));
		case 500:
			throw new InternalServerErrorException(getBodyAsMessage(response));
		default:
			throw new DockerException(getBodyAsMessage(response), status);
		}
    }

	public String getBodyAsMessage(ClientResponse response) throws IOException {
	    if (response.hasEntity()) {
	        int contentLength = response.getLength();
	        if (contentLength != -1) {
	            byte[] buffer = new byte[contentLength];
	            try {
	                IOUtils.readFully(response.getEntityInputStream(), buffer);
	            }
	            catch (EOFException e) {
	                return null;
	            }
	            Charset charset = null;
	            MediaType mediaType = response.getType();
	            if (mediaType != null) {
	                String charsetName = mediaType.getParameters().get("charset");
	                if (charsetName != null) {
	                    try {
	                        charset = Charset.forName(charsetName);
                        }
                        catch (Exception e) {
                            //Do noting...
                        }
	                }
	            }
	            if (charset == null) {
                    charset = Charset.defaultCharset();
	            }
	            String message = new String(buffer, charset);
	            return message;
	        }
	    }
	    return null;
	}
}
