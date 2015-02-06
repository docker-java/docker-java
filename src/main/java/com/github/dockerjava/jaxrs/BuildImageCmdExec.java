package com.github.dockerjava.jaxrs;



import static javax.ws.rs.client.Entity.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.model.EventStreamItem;
import com.github.dockerjava.api.model.PushEventStreamItem;

import jersey.repackaged.com.google.common.collect.ImmutableList;

public class BuildImageCmdExec extends AbstrDockerCmdExec<BuildImageCmd, BuildImageCmd.Response> implements BuildImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BuildImageCmdExec.class);
	
	public BuildImageCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected ResponseImpl execute(BuildImageCmd command) {
		WebTarget webResource = getBaseResource().path("/build");
		
		if(command.getTag() != null) {
			webResource = webResource.queryParam("t", command.getTag());
		}
        if (command.hasNoCacheEnabled()) {
            webResource = webResource.queryParam("nocache", "true");
        }
        if (command.hasRemoveEnabled()) {
            webResource = webResource.queryParam("rm", "true");
        }
        if (command.isQuiet()) {
            webResource = webResource.queryParam("q", "true");
        }
		

	  webResource.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.CHUNKED);
	  webResource.property(ClientProperties.CHUNKED_ENCODING_SIZE, 1024*1024);


          LOGGER.debug("POST: {}", webResource);
          InputStream is = webResource
              .request()
              .accept(MediaType.TEXT_PLAIN)
              .post(entity(command.getTarInputStream(), "application/tar"), Response.class).readEntity(InputStream.class);

	  return new ResponseImpl(is);
		
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
      
      proxy.close();

      return ImmutableList.copyOf(items);
    }

    @Override
    public int read() throws IOException {
      return proxy.read();
    }
  }
}
