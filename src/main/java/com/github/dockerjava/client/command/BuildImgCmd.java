package com.github.dockerjava.client.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.FileUtils;
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
	
	private static final Pattern ADD_OR_COPY_PATTERN = Pattern.compile("^(ADD|COPY)\\s+(.*)\\s+(.*)$");
	
	private static final Pattern ENV_PATTERN = Pattern.compile("^ENV\\s+(.*)\\s+(.*)$");
	
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
	
	@Override
	public String toString() {
		return new StringBuilder("build ")
			.append(tag != null ? "-t " + tag + " " : "")
			.append(noCache ? "--nocache=true " : "")
			.append(dockerFolder != null ? dockerFolder.getPath() : "-")
			.toString();
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

			Map<String, String>environmentMap = new HashMap<String, String>();
			
			int lineNumber = 0;
			
			for (String cmd : dockerFileContent) {
				
				lineNumber++;
				
				if (cmd.trim().isEmpty() || cmd.startsWith("#"))
					continue; // skip emtpy and commend lines
				
				final Matcher envMatcher = ENV_PATTERN.matcher(cmd.trim());
				
				if (envMatcher.find()) {
					if (envMatcher.groupCount() != 2)
						throw new DockerException(String.format("Wrong ENV format on line [%d]", lineNumber));
					
					String variable = envMatcher.group(1).trim();
					
					String value = envMatcher.group(2).trim();
					
					environmentMap.put(variable, value);
				}
				
				
				final Matcher matcher = ADD_OR_COPY_PATTERN.matcher(cmd.trim());
				if (matcher.find()) {
					if (matcher.groupCount() != 3) {
						throw new DockerException(String.format("Wrong ADD or COPY format on line [%d]", lineNumber));
					}

					String extractedResource = matcher.group(2);
					
					String resource = filterForEnvironmentVars(extractedResource, environmentMap);
					
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
	
	private String filterForEnvironmentVars(String extractedResource,
			Map<String, String> environmentMap) {
		
		if (environmentMap.size() > 0) {
			
			String currentResourceContent = extractedResource;
			
			for (Map.Entry<String, String> entry : environmentMap.entrySet()) {
				
				String variable = entry.getKey();
				
				String replacementValue = entry.getValue();
				
				// handle: $VARIABLE case
				currentResourceContent = currentResourceContent.replaceAll("\\$" + variable, replacementValue);
				
				// handle ${VARIABLE} case
				currentResourceContent = currentResourceContent.replaceAll("\\$\\{" + variable + "\\}", replacementValue);
				
			}
			
			return currentResourceContent;
		}
		else 
			return extractedResource;
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
