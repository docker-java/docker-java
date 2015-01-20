package com.github.dockerjava.core.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.core.CompressArchiveUtil;
import com.github.dockerjava.core.GoLangFileMatch;
import com.github.dockerjava.core.GoLangFileMatchException;
import com.github.dockerjava.core.GoLangMatchFileFilter;
import com.google.common.base.Preconditions;

/**
 * 
 * Build an image from Dockerfile.
 * 
 */
public class BuildImageCmdImpl extends
		AbstrDockerCmd<BuildImageCmd, InputStream> implements BuildImageCmd {

	private static final Pattern ADD_OR_COPY_PATTERN = Pattern
			.compile("^(ADD|COPY)\\s+(.*)\\s+(.*)$");

	private static final Pattern ENV_PATTERN = Pattern
			.compile("^ENV\\s+(.*)\\s+(.*)$");

	private InputStream tarInputStream = null;
	private File tarFile = null;
	private String tag;
	private boolean noCache;
	private boolean remove = true;
	private boolean quiet;

	public BuildImageCmdImpl(BuildImageCmd.Exec exec, File dockerFolder) {
		super(exec);
		Preconditions.checkNotNull(dockerFolder, "dockerFolder is null");
		tarFile = buildDockerFolderTar(dockerFolder);
		try {
			withTarInputStream(FileUtils.openInputStream(tarFile));
		} catch (IOException e) {
			// we just created the file this should never happen.
			throw new RuntimeException(e);
		}
	}

	public BuildImageCmdImpl(BuildImageCmd.Exec exec, InputStream tarInputStream) {
		super(exec);
		Preconditions.checkNotNull(tarInputStream, "tarInputStream is null");
		withTarInputStream(tarInputStream);
	}

	@Override
	public InputStream getTarInputStream() {
		return tarInputStream;
	}

	@Override
	public BuildImageCmdImpl withTarInputStream(InputStream tarInputStream) {
		Preconditions.checkNotNull(tarInputStream, "tarInputStream is null");
		this.tarInputStream = tarInputStream;
		return this;
	}

	@Override
	public BuildImageCmdImpl withTag(String tag) {
		Preconditions.checkNotNull(tag, "Tag is null");
		this.tag = tag;
		return this;
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public boolean hasNoCacheEnabled() {
		return noCache;
	}

	@Override
	public boolean hasRemoveEnabled() {
		return remove;
	}

	@Override
	public boolean isQuiet() {
		return quiet;
	}

	@Override
	public BuildImageCmdImpl withNoCache() {
		return withNoCache(true);
	}

	@Override
	public BuildImageCmdImpl withNoCache(boolean noCache) {
		this.noCache = noCache;
		return this;
	}

	@Override
	public BuildImageCmdImpl withRemove() {
		return withRemove(true);
	}

	@Override
	public BuildImageCmdImpl withRemove(boolean rm) {
		this.remove = rm;
		return this;
	}

	@Override
	public BuildImageCmdImpl withQuiet() {
		return withQuiet(true);
	}

	@Override
	public BuildImageCmdImpl withQuiet(boolean quiet) {
		this.quiet = quiet;
		return this;
	}

	@Override
	public void close() throws IOException {
		super.close();
		if (tarFile != null) {
			FileUtils.deleteQuietly(tarFile);
		}

		tarInputStream.close();
	}

	@Override
	public String toString() {
		return new StringBuilder("build ")
				.append(tag != null ? "-t " + tag + " " : "")
				.append(noCache ? "--nocache=true " : "")
				.append(quiet ? "--quiet=true " : "")
				.append(!remove ? "--rm=false " : "").toString();
	}

	protected File buildDockerFolderTar(File dockerFolder) {
		Preconditions.checkArgument(dockerFolder.exists(),
				"Path %s doesn't exist", dockerFolder);
		Preconditions.checkArgument(dockerFolder.isDirectory(),
				"Folder %s doesn't exist", dockerFolder);
		Preconditions.checkState(new File(dockerFolder, "Dockerfile").exists(),
				"Dockerfile doesn't exist in " + dockerFolder);

		// ARCHIVE TAR
		String archiveNameWithOutExtension = UUID.randomUUID().toString();

		File dockerFolderTar = null;

		try {
			File dockerFile = new File(dockerFolder, "Dockerfile");
			List<String> dockerFileContent = FileUtils.readLines(dockerFile);

			if (dockerFileContent.size() <= 0) {
				throw new DockerClientException(String.format(
						"Dockerfile %s is empty", dockerFile));
			}

			List<String> ignores = new ArrayList<String>();
			File dockerIgnoreFile = new File(dockerFolder, ".dockerignore");
			if (dockerIgnoreFile.exists()) {
				int lineNumber = 0;
				List<String> dockerIgnoreFileContent = FileUtils
						.readLines(dockerIgnoreFile);
				for (String pattern : dockerIgnoreFileContent) {
					lineNumber++;
					pattern = pattern.trim();
					if (pattern.isEmpty()) {
						continue; // skip empty lines
					}
					pattern = FilenameUtils.normalize(pattern);
					try {
						// validate pattern and make sure we aren't excluding
						// Dockerfile
						if (GoLangFileMatch.match(pattern, "Dockerfile")) {
							throw new DockerClientException(
									String.format(
											"Dockerfile is excluded by pattern '%s' on line %s in .dockerignore file",
											pattern, lineNumber));
						}
						ignores.add(pattern);
					} catch (GoLangFileMatchException e) {
						throw new DockerClientException(
								String.format(
										"Invalid pattern '%s' on line %s in .dockerignore file",
										pattern, lineNumber));
					}
				}
			}
			List<File> filesToAdd = new ArrayList<File>();
			filesToAdd.add(dockerFile);

			Map<String, String> environmentMap = new HashMap<String, String>();

			int lineNumber = 0;

			for (String cmd : dockerFileContent) {

				lineNumber++;

				if (cmd.trim().isEmpty() || cmd.startsWith("#"))
					continue; // skip emtpy and commend lines

				final Matcher envMatcher = ENV_PATTERN.matcher(cmd.trim());

				if (envMatcher.find()) {
					if (envMatcher.groupCount() != 2)
						throw new DockerClientException(String.format(
								"Wrong ENV format on line [%d]", lineNumber));

					String variable = envMatcher.group(1).trim();

					String value = envMatcher.group(2).trim();

					environmentMap.put(variable, value);
				}

				final Matcher matcher = ADD_OR_COPY_PATTERN.matcher(cmd.trim());
				if (matcher.find()) {
					if (matcher.groupCount() != 3) {
						throw new DockerClientException(String.format(
								"Wrong ADD or COPY format on line [%d]",
								lineNumber));
					}

					String extractedResource = matcher.group(2);

					String resource = filterForEnvironmentVars(
							extractedResource, environmentMap).trim();

					if (isFileResource(resource)) {
						File src = new File(resource);
						if (!src.isAbsolute()) {
							src = new File(dockerFolder, resource)
									.getCanonicalFile();
						} else {
							throw new DockerClientException(String.format(
									"Source file %s must be relative to %s",
									src, dockerFolder));
						}

						// if (!src.exists()) {
						// throw new DockerClientException(String.format(
						// "Source file %s doesn't exist", src));
						// }
						if (src.isDirectory()) {
							Collection<File> files = FileUtils.listFiles(src,
									new GoLangMatchFileFilter(src, ignores),
									TrueFileFilter.INSTANCE);
							filesToAdd.addAll(files);
						} else if (!src.exists()) {
							filesToAdd.addAll(resolveWildcards(src, ignores));
						} else if (!GoLangFileMatch.match(ignores,
								CompressArchiveUtil.relativize(dockerFolder,
										src))) {
							filesToAdd.add(src);
						} else {
							throw new DockerClientException(
									String.format(
											"Source file %s is excluded by .dockerignore file",
											src));
						}
					}
				}
			}

			return CompressArchiveUtil.archiveTARFiles(dockerFolder,
					filesToAdd, archiveNameWithOutExtension);
		} catch (IOException ex) {
			FileUtils.deleteQuietly(dockerFolderTar);
			throw new DockerClientException(
					"Error occurred while preparing Docker context folder.", ex);
		}
	}

	private Collection<File> resolveWildcards(File file, List<String> ignores) {
		List<File> filesToAdd = new ArrayList<File>();

		File parent = file.getParentFile();
		if (parent != null) {
			if (parent.isDirectory()) {
				Collection<File> files = FileUtils.listFiles(parent,
						new GoLangMatchFileFilter(parent, ignores),
						TrueFileFilter.INSTANCE);
				filesToAdd.addAll(files);
			} else {
				filesToAdd.addAll(resolveWildcards(parent, ignores));
			}
		} else {
			throw new DockerClientException(String.format(
					"Source file %s doesn't exist", file));
		}

		return filesToAdd;
	}

	private String filterForEnvironmentVars(String extractedResource,
			Map<String, String> environmentMap) {

		if (environmentMap.size() > 0) {

			String currentResourceContent = extractedResource;

			for (Map.Entry<String, String> entry : environmentMap.entrySet()) {

				String variable = entry.getKey();

				String replacementValue = entry.getValue();

				// handle: $VARIABLE case
				currentResourceContent = currentResourceContent.replaceAll(
						"\\$" + variable, replacementValue);

				// handle ${VARIABLE} case
				currentResourceContent = currentResourceContent.replaceAll(
						"\\$\\{" + variable + "\\}", replacementValue);

			}

			return currentResourceContent;
		} else
			return extractedResource;
	}

	private static boolean isFileResource(String resource) {
		URI uri;
		try {
			uri = new URI(resource);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return uri.getScheme() == null || "file".equals(uri.getScheme());
	}
}
