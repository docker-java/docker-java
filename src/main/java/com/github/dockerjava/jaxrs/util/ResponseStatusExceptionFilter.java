package com.github.dockerjava.jaxrs.util;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

import org.apache.commons.io.IOUtils;

import com.github.dockerjava.api.BadRequestException;
import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.InternalServerErrorException;
import com.github.dockerjava.api.NotAcceptableException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.UnauthorizedException;

/**
 * This {@link ClientResponseFilter} implementation detects http status codes and throws {@link DockerException}s 
 *
 * @author marcus
 *
 */
public class ResponseStatusExceptionFilter implements ClientResponseFilter {


    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
    	int status = responseContext.getStatus();
		switch (status) {
		case 200:
		case 201:	
		case 204:
			return;
		case 304:
			throw new NotModifiedException(getBodyAsMessage(responseContext));
		case 400:
			throw new BadRequestException(getBodyAsMessage(responseContext));
		case 401:
			throw new UnauthorizedException(getBodyAsMessage(responseContext));	
		case 404:
			throw new NotFoundException(getBodyAsMessage(responseContext));
		case 406:
			throw new NotAcceptableException(getBodyAsMessage(responseContext));
		case 409:
			throw new ConflictException(getBodyAsMessage(responseContext));
		case 500: {
			
			throw new InternalServerErrorException(getBodyAsMessage(responseContext));
		}	
		default:
			throw new DockerException(getBodyAsMessage(responseContext), status);
		}
    }

	public String getBodyAsMessage(ClientResponseContext responseContext)
			throws IOException {
		byte[] buffer = new byte[1000];
		IOUtils.read(responseContext.getEntityStream(), buffer);
		String message = new String(buffer);
		return message;
	}
}
