package com.github.dockerjava.jaxrs.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.exception.BadRequestException;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotAcceptableException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.exception.UnauthorizedException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * This {@link ClientResponseFilter} implementation detects http status codes and throws {@link DockerException}s
 *
 * @author Marcus Linke
 *
 */
@Deprecated
public class ResponseStatusExceptionFilter implements ClientResponseFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseStatusExceptionFilter.class);

    private final ObjectMapper objectMapper;

    @Deprecated
    public ResponseStatusExceptionFilter() {
        this(new ObjectMapper());
    }

    public ResponseStatusExceptionFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

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
            case 500:
                throw new InternalServerErrorException(getBodyAsMessage(responseContext));
            default:
                throw new DockerException(getBodyAsMessage(responseContext), status);
        }
    }

    private String getBodyAsMessage(ClientResponseContext responseContext) {
        if (responseContext.hasEntity()) {
            try (InputStream entityStream = responseContext.getEntityStream()) {
                Charset charset = null;
                MediaType mediaType = responseContext.getMediaType();
                if (mediaType != null) {
                    String charsetName = mediaType.getParameters().get("charset");
                    if (charsetName != null) {
                        try {
                            charset = Charset.forName(charsetName);
                        } catch (Exception ignored) { }
                    }
                }

                if (charset == null) {
                    charset = Charset.defaultCharset();
                }

                String message = IOUtils.toString(entityStream, charset);

                if (MediaType.APPLICATION_JSON_TYPE.equals(mediaType)) {
                    try {
                        JsonNode node = objectMapper.readTree(message);
                        if (node != null) {
                            JsonNode messageNode = node.get("message");
                            if (messageNode != null && messageNode.isTextual()) {
                                message = messageNode.textValue();
                            }
                        }
                    } catch (IOException e) {
                        // ignore parsing errors and return the message as is
                        LOG.debug("Failed to unwrap error message: {}", e.getMessage(), e);
                    }
                }
                return message;
            } catch (Exception ignored) { }
        }
        return null;
    }
}
