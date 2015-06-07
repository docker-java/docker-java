package com.github.dockerjava.jaxrs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.EventStreamItem;
import com.github.dockerjava.jaxrs.util.WrappedResponseInputStream;
import com.google.common.collect.ImmutableList;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import static javax.ws.rs.client.Entity.entity;

public class BuildImageCmdExec extends
		AbstrDockerCmdExec<BuildImageCmd, BuildImageCmd.Response> implements
		BuildImageCmd.Exec {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BuildImageCmdExec.class);

	public BuildImageCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected ResponseImpl execute(BuildImageCmd command) {
		WebTarget webResource = getBaseResource().path("/build");
		String dockerFilePath = command.getPathToDockerfile();

		if (command.getImageName() != null) {
			webResource = webResource.queryParam("t", command.getImageName());
		}
		if (command.hasNoCacheEnabled()) {
			webResource = webResource.queryParam("nocache", "true");
		}
		if (!command.hasRemoveEnabled()) {
			webResource = webResource.queryParam("rm", "false");
		}
		if (command.isQuiet()) {
			webResource = webResource.queryParam("q", "true");
		}
		if (command.hasPullEnabled()) {
			webResource = webResource.queryParam("pull", "true");
		}
		if (dockerFilePath != null && !"Dockerfile".equals(dockerFilePath)) {
			webResource = webResource.queryParam("dockerfile", dockerFilePath);
		}

		webResource.property(ClientProperties.REQUEST_ENTITY_PROCESSING,
				RequestEntityProcessing.CHUNKED);
		webResource.property(ClientProperties.CHUNKED_ENCODING_SIZE,
				1024 * 1024);

		LOGGER.debug("POST: {}", webResource);
		Response response = resourceWithOptionalAuthConfig(command,
				webResource.request())
				.accept(MediaType.TEXT_PLAIN)
				.post(entity(command.getTarInputStream(), "application/tar"),
						Response.class);

		return new ResponseImpl(new WrappedResponseInputStream(response));

	}

	private Invocation.Builder resourceWithOptionalAuthConfig(
			BuildImageCmd command, Invocation.Builder request) {
		AuthConfigurations authConfigs = command.getBuildAuthConfigs();
		if (authConfigs != null) {
			request = request.header("X-Registry-Config",
					registryConfigs(authConfigs));
		}
		return request;
	}

	public static class ResponseImpl extends BuildImageCmd.Response {

		private final InputStream proxy;

		public ResponseImpl(InputStream proxy) {
			this.proxy = proxy;
		}

		@Override
		public Iterable<EventStreamItem> getItems() throws IOException {
			ObjectMapper mapper = new ObjectMapper();
			// we'll be reading instances of MyBean
			ObjectReader reader = mapper.reader(EventStreamItem.class);
			// and then do other configuration, if any, and read:
			Iterator<EventStreamItem> items = reader.readValues(proxy);

			try {
				return ImmutableList.copyOf(items);
			} finally {
				proxy.close();
			}
		}

		@Override
		public int read() throws IOException {
			return proxy.read();
		}

		@Override
		public void close() throws IOException {
			proxy.close();
			super.close();
		}
	}
}
