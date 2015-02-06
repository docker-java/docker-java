package com.github.dockerjava.core.dockerfile;

import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.core.CompressArchiveUtil;
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

import jersey.repackaged.com.google.common.base.Function;
import jersey.repackaged.com.google.common.base.Objects;
import jersey.repackaged.com.google.common.base.Optional;
import jersey.repackaged.com.google.common.base.Predicate;
import jersey.repackaged.com.google.common.collect.Collections2;

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

  /**
   * Not needed in modern guava
   */
  private static class MissingOptionalFilter
      implements Predicate<Optional<? extends DockerfileStatement>> {

    @Override
    public boolean apply(Optional<? extends DockerfileStatement> optional) {
      return (optional.orNull() != null);
    }
  }

  /**
   * Not needed in modern guava
   */
  private static class OptionalItemTransformer
      implements Function<Optional<? extends DockerfileStatement>, DockerfileStatement> {

    @Override
    public DockerfileStatement apply(Optional<? extends DockerfileStatement> optional) {
      return optional.orNull();
    }
  }

  public Collection<DockerfileStatement> getStatements() throws IOException {
    Collection<String> dockerFileContent = FileUtils.readLines(dockerFile);

    if (dockerFileContent.size() <= 0) {
      throw new DockerClientException(String.format(
          "Dockerfile %s is empty", dockerFile));
    }

    Collection<Optional<? extends DockerfileStatement>> optionals = Collections2
        .transform(dockerFileContent, new LineTransformer());

    // Modern guava would be done here,
    // With simply return Optional.presentInstances( optionals );
    //
    // So this entire function could simply be
    // return Optional.presentInstances( Collections2.transform( FileUtils.readLines(dockerFile), new LineTransformer() ) );
    //
    // Until the dawn of that day, do it manually

    return Collections2.transform(Collections2.filter(optionals, new MissingOptionalFilter()),
                                  new OptionalItemTransformer());

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

      // ARCHIVE TAR
      File dockerFolderTar = null;

      try {
        String archiveNameWithOutExtension = UUID.randomUUID().toString();

        dockerFolderTar = CompressArchiveUtil.archiveTARFiles(getDockerFolder(),
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

      for (DockerfileStatement statement : getStatements()) {
        if (statement instanceof DockerfileStatement.Env) {
          processEnvStatement((DockerfileStatement.Env) statement);
        } else if (statement instanceof DockerfileStatement.Add) {
          processAddStatement((DockerfileStatement.Add) statement);
        }
      }
    }

    private void processAddStatement(DockerfileStatement.Add add) throws IOException {

      add = add.transform(environmentMap);

      if (add.isFileResource()) {

        File dockerFolder = getDockerFolder();
        String resource = add.source;

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

    private void processEnvStatement(DockerfileStatement.Env env) {

      environmentMap.put(env.variable, env.value);
    }

  }

}
