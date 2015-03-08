package com.github.dockerjava.core.http;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

public abstract class AbstractHttpFeature implements ClientRequestFilter {
	
	@Override
	public abstract void filter(ClientRequestContext requestContext) throws IOException;
	public abstract Map<String, Object> getClientConfigurationProperties();
}
