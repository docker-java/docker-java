package com.github.dockerjava.core.dockerfile;

import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.core.CompressArchiveUtil;
import com.github.dockerjava.core.FilePathUtil;
import com.github.dockerjava.core.GoLangFileMatch;
import com.github.dockerjava.core.GoLangFileMatchException;
import com.github.dockerjava.core.GoLangMatchFileFilter;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
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

/**
 * Parse a Dockerfile.
 */
public class Dockerfile {

    public final File dockerFile;

    private final File baseDirectory;

    public Dockerfile(File dockerFile, File baseDirectory) {
        if (!dockerFile.exists()) {
            throw new IllegalStateException(String.format("Dockerfile %s does not exist", dockerFile.getAbsolutePath()));
        }

        if (!dockerFile.isFile()) {
            throw new IllegalStateException(String.format("Dockerfile %s is not a file", dockerFile.getAbsolutePath()));
        }

        this.dockerFile = dockerFile;

        if (!baseDirectory.exists()) {
            throw new IllegalStateException(String.format("Base directory %s does not exist", baseDirectory.getAbsolutePath()));
        }

        if (!baseDirectory.isDirectory()) {
            throw new IllegalStateException(String.format("Base directory %s is not a directory", baseDirectory.getAbsolutePath()));
        }

        this.baseDirectory = baseDirectory;
    }

    private static class LineTransformer implements Function<String, Optional<? extends DockerfileStatement>> {

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
            throw new DockerClientException(String.format("Dockerfile %s is empty", dockerFile));
        }

        Collection<Optional<? extends DockerfileStatement>> optionals = Collections2.transform(dockerFileContent,
                new LineTransformer());

        return Optional.presentInstances(optionals);
    }

    public List<String> getIgnores() throws IOException {
        List<String> ignores = new ArrayList<String>();
        File dockerIgnoreFile = new File(baseDirectory, ".dockerignore");
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

    /**
     * Result of scanning / parsing a docker file.
     */
    public class ScannedResult {

        final List<String> ignores;

        final Map<String, String> environmentMap = new HashMap<String, String>();

        final List<File> filesToAdd = new ArrayList<File>();

        public InputStream buildDockerFolderTar() {
            return buildDockerFolderTar(baseDirectory);
        }

        public InputStream buildDockerFolderTar(File directory) {

            File dockerFolderTar = null;

            try {
                final String archiveNameWithOutExtension = UUID.randomUUID().toString();

                dockerFolderTar = CompressArchiveUtil.archiveTARFiles(directory, filesToAdd,
                        archiveNameWithOutExtension);

                final InputStream tarInputStream = FileUtils.openInputStream(dockerFolderTar);
                final File tarFile = dockerFolderTar;

                return new InputStream() {
                    @Override
                    public int read() throws IOException {
                        return tarInputStream.read();
                    }

                    @Override
                    public int read(byte [] buff, int offset, int len) throws IOException {
                        return tarInputStream.read(buff, offset, len);
                    }

                    @Override
                    public void close() throws IOException {
                        IOUtils.closeQuietly(tarInputStream);
                        FileUtils.deleteQuietly(tarFile);
                    }
                };

            } catch (IOException ex) {
                FileUtils.deleteQuietly(dockerFolderTar);
                throw new DockerClientException("Error occurred while preparing Docker context folder.", ex);
            }
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this).add("ignores", ignores).add("environmentMap", environmentMap)
                    .add("filesToAdd", filesToAdd).toString();
        }

        public ScannedResult() throws IOException {

            ignores = getIgnores();

            String matchingIgnorePattern = effectiveMatchingIgnorePattern(dockerFile);

            if (matchingIgnorePattern == null) {
                filesToAdd.add(dockerFile);
            } else {
                throw new DockerClientException(String.format(
                        "Dockerfile is excluded by pattern '%s' in .dockerignore file", matchingIgnorePattern));
            }

            for (DockerfileStatement statement : getStatements()) {
                if (statement instanceof DockerfileStatement.Env) {
                    processEnvStatement((DockerfileStatement.Env) statement);
                } else if (statement instanceof DockerfileStatement.Add) {
                    processAddStatement((DockerfileStatement.Add) statement);
                }
            }
        }

        /**
         * Returns all matching ignore patterns for the given file name.
         */
        private List<String> matchingIgnorePatterns(String fileName) {
            List<String> matches = new ArrayList<String>();

            int lineNumber = 0;
            for (String pattern : ignores) {
                lineNumber++;
                try {
                    if (GoLangFileMatch.match(pattern, fileName)) {
                        matches.add(pattern);
                    }
                } catch (GoLangFileMatchException e) {
                    throw new DockerClientException(String.format(
                            "Invalid pattern '%s' on line %s in .dockerignore file", pattern, lineNumber));
                }
            }

            return matches;
        }

        /**
         * Returns the matching ignore pattern for the given file or null if it should NOT be ignored. Exception rules
         * like "!Dockerfile" will be respected.
         */
        private String effectiveMatchingIgnorePattern(File file) {
            String relativeFilename = FilePathUtil.relativize(baseDirectory, file);

            List<String> matchingPattern = matchingIgnorePatterns(relativeFilename);

            if (matchingPattern.isEmpty())
                return null;

            String lastMatchingPattern = matchingPattern.get(matchingPattern.size() - 1);

            int lastMatchingPatternIndex = ignores.lastIndexOf(lastMatchingPattern);

            if (lastMatchingPatternIndex == ignores.size() - 1)
                return lastMatchingPattern;

            List<String> remainingIgnorePattern = ignores.subList(lastMatchingPatternIndex + 1, ignores.size());

            for (String ignorePattern : remainingIgnorePattern) {
                if (ignorePattern.equals("!" + relativeFilename))
                    return null;
            }

            return lastMatchingPattern;
        }

        private void processAddStatement(DockerfileStatement.Add add) throws IOException {

            add = add.transform(environmentMap);

            for (String resource : add.getFileResources()) {

                File src = new File(resource);
                if (!src.isAbsolute()) {
                    src = new File(baseDirectory, resource);
                } else {
                    throw new DockerClientException(String.format("Source file %s must be relative to %s", src,
                            baseDirectory));
                }

                if (src.exists()) {
                    src = src.getCanonicalFile();
                    if (src.isDirectory()) {
                        Collection<File> files = FileUtils.listFiles(src, new GoLangMatchFileFilter(src, ignores),
                                TrueFileFilter.INSTANCE);
                        filesToAdd.addAll(files);
                    } else if (effectiveMatchingIgnorePattern(src) == null) {
                        filesToAdd.add(src);
                    } else {
                        throw new DockerClientException(String.format(
                                "Source file %s is excluded by .dockerignore file", src));
                    }
                } else {
                    filesToAdd.addAll(resolveWildcards(src, ignores));
                }
            }
        }

        private Collection<File> resolveWildcards(File file, List<String> ignores) {
            List<File> filesToAdd = new ArrayList<File>();

            File parent = file.getParentFile();
            if (parent != null) {
                if (parent.isDirectory()) {
                    Collection<File> files = FileUtils.listFiles(parent, new GoLangMatchFileFilter(parent, ignores),
                            TrueFileFilter.INSTANCE);
                    filesToAdd.addAll(files);
                } else {
                    filesToAdd.addAll(resolveWildcards(parent, ignores));
                }
            } else {
                throw new DockerClientException(String.format("Source file %s doesn't exist", file));
            }

            return filesToAdd;
        }

        private void processEnvStatement(DockerfileStatement.Env env) {
            environmentMap.put(env.variable, env.value);
        }
    }
}
