package com.github.dockerjava.jaxrs.util;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.stream.ObjectStreamCallback;

public class Streaming {
	
	private static final JsonFactory JSON_FACTORY = new JsonFactory();
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	public static <T> void processJsonStream(Response response,
			ObjectStreamCallback<T> objectStreamCallback, Class<T> clazz) {
		
		InputStream inputStream = new WrappedResponseInputStream(
				response);
		
		objectStreamCallback.streamStarted(inputStream);
		
		try {
			JsonParser jp = JSON_FACTORY.createParser(inputStream);
			while (!jp.isClosed() && jp.nextToken() != JsonToken.END_OBJECT) {
				try {
					objectStreamCallback.onStream(OBJECT_MAPPER.readValue(jp,
							clazz));
				} catch (Exception e) {
					objectStreamCallback.onError(e);
				}
			}
		} catch (Throwable t) {
			objectStreamCallback.onError(t);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				objectStreamCallback.onError(e);
			} finally {
				objectStreamCallback.streamFinished();
			}
		}
	}
	


}
