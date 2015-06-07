package com.github.dockerjava.jaxrs;



import static javax.ws.rs.client.Entity.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.command.PushImageCmd.Response;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PushEventStreamItem;

import com.github.dockerjava.jaxrs.util.WrappedResponseInputStream;
// Shaded, but imported
import com.google.common.collect.ImmutableList;

public class PushImageCmdExec extends AbstrDockerCmdExec<PushImageCmd, Response> implements PushImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PushImageCmdExec.class);
	
	public PushImageCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected ResponseImpl execute(PushImageCmd command) {
		WebTarget webResource = getBaseResource().path("/images/" + name(command) + "/push")
		    .queryParam("tag", command.getImageName());

		final String registryAuth = registryAuth(command.getAuthConfig());
		LOGGER.trace("POST: {}", webResource);
		javax.ws.rs.core.Response response =  webResource
                .request()
				.header("X-Registry-Auth", registryAuth)
				.accept(MediaType.APPLICATION_JSON)
				.post(
				    entity(Response.class, MediaType.APPLICATION_JSON));

	  return new ResponseImpl(new WrappedResponseInputStream(response));
	}
	
	private String name(PushImageCmd command) {
		String name = command.getImageName().toString();
		AuthConfig authConfig = command.getAuthConfig();
		return name.contains("/") ? name : authConfig.getUsername();
	}


  public static class ResponseImpl extends Response {

    private final InputStream proxy;

    ResponseImpl(InputStream proxy) {
      this.proxy = proxy;
    }

    @Override
    public Iterable<PushEventStreamItem> getItems() throws IOException {
      ObjectMapper mapper = new ObjectMapper();
      // we'll be reading instances of MyBean
      ObjectReader reader = mapper.reader(PushEventStreamItem.class);
      // and then do other configuration, if any, and read:
      Iterator<PushEventStreamItem> items = reader.readValues(proxy);

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
  }
}
