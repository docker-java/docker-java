package com.github.dockerjava.client.command;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Create an image by importing the given stream of a tar file.
 */
public class CreateImageCmd extends	AbstrDockerCmd<CreateImageCmd, CreateImageResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateImageCmd.class);

	private String repository, tag;
	private InputStream imageStream;

	/**
	 * @param repository        the repository to import to
	 * @param imageStream       the InputStream of the tar file
	 */
	public CreateImageCmd(String repository, InputStream imageStream) {
		withRepository(repository);
		withImageStream(imageStream);
	}

    public String getRepository() {
        return repository;
    }

    public String getTag() {
        return tag;
    }

    /**
	 * @param repository        the repository to import to
	 */
	public CreateImageCmd withRepository(String repository) {
		Preconditions.checkNotNull(repository, "repository was not specified");
		this.repository = repository;
		return this;
	}

	/**
	 * @param imageStream       the InputStream of the tar file
	 */
	public CreateImageCmd withImageStream(InputStream imageStream) {
		Preconditions
				.checkNotNull(imageStream, "imageStream was not specified");
		this.imageStream = imageStream;
		return this;
	}

	/**
	 * @param tag               any tag for this image
	 */
	public CreateImageCmd withTag(String tag) {
		Preconditions.checkNotNull(tag, "tag was not specified");
		this.tag = tag;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("import - ")
            .append(repository != null ? repository + ":" : "")
            .append(tag != null ? tag : "")
            .toString();
    }

	protected CreateImageResponse impl() {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("repo", repository);
		params.add("tag", tag);
		params.add("fromSrc", "-");

		WebResource webResource = baseResource.path("/images/create").queryParams(params);

		try {
			LOGGER.trace("POST: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
					.post(CreateImageResponse.class, imageStream);

		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error.", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}
}
