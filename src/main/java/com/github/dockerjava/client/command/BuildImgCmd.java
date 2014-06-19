package com.github.dockerjava.client.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.utils.CompressArchiveUtil;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * 
 * Build an image from Dockerfile.
 *
 */
public class BuildImgCmd extends AbstrDockerCmd<BuildImgCmd, ClientResponse>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(BuildImgCmd.class);
	
	private File dockerFolder = null;
	private InputStream tarInputStream = null;
	private String tag;
	private boolean noCache;
	
	
	public BuildImgCmd(File dockerFolder) {
		Preconditions.checkNotNull(dockerFolder, "dockerFolder is null");
		this.dockerFolder = dockerFolder;
	}
	
	public BuildImgCmd(InputStream tarInputStream) {
		Preconditions.checkNotNull(tarInputStream, "tarInputStream is null");
		this.tarInputStream = tarInputStream;
	}
	
	public BuildImgCmd withTag(String tag) {
		Preconditions.checkNotNull(tag, "Tag is null");
		this.tag = tag;
		return this;
	}
	
	public BuildImgCmd withNoCache(boolean noCache) {
		this.noCache = noCache;
		return this;
	}
	
	protected ClientResponse impl() {
		if (tarInputStream == null) {
			File dockerFolderTar = buildDockerFolderTar();
			try {
				return callDocker(FileUtils.openInputStream(dockerFolderTar));
			} catch (IOException e) {
				throw new DockerException(e);
			} finally {
				FileUtils.deleteQuietly(dockerFolderTar);
			}
		} else {
			return callDocker(tarInputStream);
		}
	}

	protected ClientResponse callDocker(final InputStream dockerFolderTarInputStream) {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("t", tag);
		if (noCache) {
			params.add("nocache", "true");
		}

		WebResource webResource = baseResource.path("/build").queryParams(params);

		try {
			LOGGER.trace("POST: {}", webResource);
			return webResource
					.type("application/tar")
					.accept(MediaType.TEXT_PLAIN)
					.post(ClientResponse.class, dockerFolderTarInputStream);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}
	
	protected File buildDockerFolderTar() {
		Preconditions.checkArgument(dockerFolder.exists(), "Path %s doesn't exist", dockerFolder);
		Preconditions.checkArgument(dockerFolder.isDirectory(), "Folder %s doesn't exist", dockerFolder);
		Preconditions.checkState(new File(dockerFolder, "Dockerfile").exists(), "Dockerfile doesn't exist in " + dockerFolder);
		
		// ARCHIVE TAR
		String archiveNameWithOutExtension = UUID.randomUUID().toString();

		File dockerFolderTar = null;

		try {
			File dockerFile = new File(dockerFolder, "Dockerfile");
			List<String> dockerFileContent = FileUtils.readLines(dockerFile);

			if (dockerFileContent.size() <= 0) {
				throw new DockerException(String.format("Dockerfile %s is empty", dockerFile));
			}

			List<File> filesToAdd = new ArrayList<File>();
			filesToAdd.add(dockerFile);

			for (String cmd : dockerFileContent) {
				if (StringUtils.startsWithIgnoreCase(cmd.trim(), "ADD")) {
					String addArgs[] = StringUtils.split(cmd, " \t");
					if (addArgs.length != 3) {
						throw new DockerException(String.format("Wrong format on line [%s]", cmd));
					}

					String resource = addArgs[1];
					
					if(isFileResource(resource)) {
						File src = new File(resource);
						if (!src.isAbsolute()) {
							src = new File(dockerFolder, resource).getCanonicalFile();
						} else {
							throw new DockerException(String.format("Source file %s must be relative to %s", src, dockerFolder));
						}

						if (!src.exists()) {
							throw new DockerException(String.format("Source file %s doesn't exist", src));
						}
						if (src.isDirectory()) {
							filesToAdd.addAll(FileUtils.listFiles(src, null, true));
						} else {
							filesToAdd.add(src);
						}
					} 
				}
			}

			dockerFolderTar = CompressArchiveUtil.archiveTARFiles(dockerFolder, filesToAdd, archiveNameWithOutExtension);
			return dockerFolderTar;
		} catch (IOException ex) {
			FileUtils.deleteQuietly(dockerFolderTar);
			throw new DockerException("Error occurred while preparing Docker context folder.", ex);
		}
	}
	
	private static boolean isFileResource(String resource)  {
        URI uri;
		try {
			uri = new URI(resource);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
        return uri.getScheme() == null || "file".equals(uri.getScheme());
    }
}
