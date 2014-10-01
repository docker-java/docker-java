package com.github.dockerjava.jaxrs1;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.DockerCmd;
import com.github.dockerjava.api.command.DockerCmdExec;
import com.github.dockerjava.api.model.AuthConfig;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.uri.UriTemplate;

public abstract class AbstrDockerCmdExec<CMD_T extends DockerCmd<RES_T>, RES_T>
		implements DockerCmdExec<CMD_T, RES_T> {

	private WebResource baseResource;

	public AbstrDockerCmdExec(WebResource baseResource) {
		Preconditions.checkNotNull(baseResource,
				"baseResource was not specified");
		this.baseResource = baseResource;
	}

	protected WebResourceBuilder getBaseResource() {
	    return new WebResourceBuilder(baseResource);
	}
	
	public static class WebResourceBuilder {
	    WebResource resource;
	    
	    private WebResourceBuilder(WebResource resource) {
	        this.resource = resource;
	    }
	    
	    public WebResourceBuilder path(String pathStr) {
	        this.resource = this.resource.path(pathStr);
	        return this;
	    }
	    
	    public WebResourceBuilder resolveTemplate(String path, String... values) {
	        UriTemplate tmplt = new UriTemplate(path);
	        this.resource = this.resource.path(tmplt.createURI(values));
	        return this;
	    }
	    
	    public WebResourceBuilder queryParam(String name, String value) {
	        if (value != null) {
	            this.resource = this.resource.queryParam(name, value);
	        }
	        return this;
	    }
	    
	    public WebResource build() {
	        return this.resource;
	    }
	}

	protected String registryAuth(AuthConfig authConfig) {
		try {
			return Base64.encodeBase64String(new ObjectMapper()
					.writeValueAsString(authConfig).getBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public RES_T exec(CMD_T command) {
		// this hack works because of ResponseStatusExceptionFilter
		RES_T result;
		try {
			result = execute(command);
		} catch (ClientHandlerException e) {
			if(e.getCause() instanceof DockerException) {
				throw (DockerException)e.getCause();
			} else {
				throw e;
			}
		}
		return result;
	}

	protected abstract RES_T execute(CMD_T command);
}
