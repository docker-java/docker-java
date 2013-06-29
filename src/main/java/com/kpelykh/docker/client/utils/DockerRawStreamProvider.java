package com.kpelykh.docker.client.utils;

import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.regex.Pattern;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@Produces("application/vnd.docker.raw-stream")
@Consumes("application/vnd.docker.raw-stream")
public class DockerRawStreamProvider extends AbstractMessageReaderWriterProvider<InputStream> {

    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return InputStream.class == type;
    }

    public InputStream readFrom(
            Class<InputStream> type,
            Type genericType,
            Annotation annotations[],
            MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders,
            InputStream entityStream) throws IOException {
        return entityStream;
    }

    public boolean isWriteable(Class<?> type, Type genericType, Annotation annotations[], MediaType mediaType) {
        return InputStream.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(InputStream t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (t instanceof ByteArrayInputStream)
            return ((ByteArrayInputStream)t).available();
        else
            return -1;
    }

    public void writeTo(
            InputStream t,
            Class<?> type,
            Type genericType,
            Annotation annotations[],
            MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException {
        try {
            writeTo(t, entityStream);
        } finally {
            t.close();
        }
    }
}