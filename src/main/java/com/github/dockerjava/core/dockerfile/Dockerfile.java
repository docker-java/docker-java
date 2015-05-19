package com.github.dockerjava.core.dockerfile;

import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.core.CompressArchiveUtil;
import com.github.dockerjava.core.FilePathUtil;
import com.github.dockerjava.core.GoLangFileMatch;
import com.github.dockerjava.core.GoLangFileMatchException;
import com.github.dockerjava.core.GoLangMatchFileFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;

/**
 * Parse a Dockerfile.
 */
public class Dockerfile {

  public final File dockerFile;

  public Dockerfile(File dockerFile) {

    if (!dockerFile.exists()) {
      throw new IllegalStateException(
          String.format("Dockerfile %s does not exist", dockerFile.getAbsolutePath()));
    }

    if (!dockerFile.isFile()) {
      throw new IllegalStateException(
          String.format("Dockerfile %s is not a file", dockerFile.getAbsolutePath()));
    }

    this.dockerFile = dockerFile;

  }

  private static class LineTransformer
      implements Function<String, Optional<? extends DockerfileStatement>> {

    private int line = 0;

    @Override
    public Optional<? extends DockerfileStatement> apply(String input) {
      try {
        line++;
        return DockerfileStatement.createFromLine(input);

      } catch (Exception ex) {
        throw new DockerClientException("Error on dockerfile line " + line);
      }
    }
  }

  public Iterable<DockerfileStatement> getStatements() throws IOException {
    Collection<String> dockerFileContent = FileUtils.readLines(dockerFile);

    if (dockerFileContent.size() <= 0) {
      throw new DockerClientException(String.format(
          "Dockerfile %s is empty", dockerFile));
    }

    Collection<Optional<? extends DockerfileStatement>> optionals = Collections2
        .transform(dockerFileContent, new LineTransformer());

    return Optional.presentInstances(optionals);
  }

  public List<String> getIgnores() throws IOException {
    List<String> ignores = new ArrayList<String>();
    File dockerIgnoreFile = new File(getDockerFolder(), ".dockerignore");
    if (dockerIgnoreFile.exists()) {
      int lineNumber = 0;
      List<String> dockerIgnoreFileContent = FileUtils.readLines(dockerIgnoreFile);
      for (String pattern : dockerIgnoreFileContent) {
        lineNumber++;
        pattern = pattern.trim();
        if (pattern.isEmpty()) {
          continue; // skip empty lines
        }
        pattern = FilenameUtils.normalize(pattern);
        try {
          // validate pattern and make sure we aren't excluding Dockerfile
          if (GoLangFileMatch.match(pattern, "Dockerfile")) {
            throw new DockerClientException(
                String.format(
                    "Dockerfile is excluded by pattern '%s' on line %s in .dockerignore file",
                    pattern, lineNumber));
          }
          ignores.add(pattern);
        } catch (GoLangFileMatchException e) {
          throw new DockerClientException(String.format(
              "Invalid pattern '%s' on line %s in .dockerignore file", pattern, lineNumber));
        }
      }
    }
    return ignores;
  }


  public ScannedResult parse() throws IOException {
    return new ScannedResult();
  }


  public File getDockerFolder() {
    return dockerFile.getParentFile();
  }


  /**
   * Result of scanning / parsing a docker file.
   */
  public class ScannedResult {

    final List<String> ignores;
    final Map<String, String> environmentMap = new HashMap<String, String>();
    final List<File> filesToAdd = new ArrayList<File>();

    public InputStream buildDockerFolderTar() {
        return buildDockerFolderTar(getDockerFolder());
    }

    public InputStream buildDockerFolderTar(File directory) {

      // ARCHIVE TAR
      File dockerFolderTar = null;

      try {
        String archiveNameWithOutExtension = UUID.randomUUID().toString();

        dockerFolderTar = CompressArchiveUtil.archiveTARFiles(directory,
                                                              filesToAdd,
                                                              archiveNameWithOutExtension);
        return FileUtils.openInputStream(dockerFolderTar);

      } catch (IOException ex) {
        FileUtils.deleteQuietly(dockerFolderTar);
        throw new DockerClientException(
            "Error occurred while preparing Docker context folder.", ex);
      }
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this)
          .add("ignores", ignores)
          .add("environmentMap", environmentMap)
          .add("filesToAdd", filesToAdd)
          .toString();
    }

    public ScannedResult() throws IOException {

      ignores = getIgnores();
      filesToAdd.add(dockerFile);

      addAllFilesToContext(dockerFile.getParentFile());

    }

    private void addAllFilesToContext(File dockerContextFolder) {
		
    	File[] files = dockerContextFolder.listFiles();
    	
    	if(files != null && files.length > 0) {
    		
    		for(File file : files) {
    			
    			filesToAdd.add(file);
    			
    			if(file.isDirectory()) {
    				
    				addAllFilesToContext(file);
    				
    			}
    			
    		}
    		
    	}
		
    }
   
  }

}
