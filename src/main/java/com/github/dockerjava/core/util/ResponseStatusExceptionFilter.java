package com.github.dockerjava.core.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
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
		case 301:	
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
		case 500:
			throw new InternalServerErrorException(getBodyAsMessage(responseContext));
		default:
			throw new DockerException(getBodyAsMessage(responseContext), status);
		}
    }

	private String getBodyAsMessage(ClientResponseContext responseContext)
			throws IOException {
	    if (responseContext.hasEntity()) {
	        int contentLength = responseContext.getLength();
	        if (contentLength != -1) {
	            byte[] buffer = new byte[contentLength];
	            try {
	                InputStream entityStream = responseContext.getEntityStream();
                  IOUtils.readFully(entityStream, buffer);
                  entityStream.close();
	            }
	            catch (EOFException e) {
	                return null;
	            }
	            Charset charset = null;
	            MediaType mediaType = responseContext.getMediaType();
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
