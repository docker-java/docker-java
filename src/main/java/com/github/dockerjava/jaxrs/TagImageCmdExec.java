package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;

import com.github.dockerjava.core.util.DockerImageName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.TagImageCmd;

public class TagImageCmdExec extends AbstrDockerCmdExec<TagImageCmd, Void> implements TagImageCmd.Exec {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TagImageCmdExec.class);
	
	public TagImageCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(TagImageCmd command) {
		WebTarget webResource = getBaseResource().path("/images/" + command.getOriginalImageName() + "/tag")
                .queryParam("repo", getImageNameWithoutTag(command.getNewImageName()))
                .queryParam("tag", command.getNewImageName().getTag())
                .queryParam("force", command.hasForceEnabled() ? "1" : "0");

		LOGGER.trace("POST: {}", webResource);
		webResource.request().post(null).close();
		return null;
	}

	/**
	 * Returns a String with all parts except Tag
	 * @param imageName
	 * @return
	 */
	private String getImageNameWithoutTag(DockerImageName imageName)
	{
		StringBuilder builder = new StringBuilder();
		if(imageName.getRegistry() != null && !imageName.getRegistry().equals(""))
		{
			builder.append(imageName.getRegistry());
			builder.append("/");
		}

		if(imageName.getNamespace() != null && !imageName.getNamespace().equals(""))
		{
			builder.append(imageName.getNamespace());
			builder.append("/");
		}

		builder.append(imageName.getRepository());

		return builder.toString();
	}
	

}
