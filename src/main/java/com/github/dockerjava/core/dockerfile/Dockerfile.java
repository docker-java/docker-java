package com.github.dockerjava.core.dockerfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.core.GoLangFileMatch;
import com.github.dockerjava.core.exception.GoLangFileMatchException;
import com.github.dockerjava.core.util.CompressArchiveUtil;
import com.github.dockerjava.core.util.FilePathUtil;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;

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

                long length = dockerFolderTar.length();

                final FileInputStream tarInputStream = FileUtils.openInputStream(dockerFolderTar);
                final File tarFile = dockerFolderTar;

                return new InputStream() {

                    @Override
                    public int available() throws IOException {
                        return tarInputStream.available();
                    }

                    @Override
                    public int read() throws IOException {
                        return tarInputStream.read();
                    }

                    @Override
                    public int read(byte[] buff, int offset, int len) throws IOException {
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
            return Objects.toStringHelper(this).add("ignores", ignores).add("filesToAdd", filesToAdd).toString();
        }

        public ScannedResult() throws IOException {

            ignores = getIgnores();

            String matchingIgnorePattern = effectiveMatchingIgnorePattern(dockerFile);

            if (matchingIgnorePattern != null) {
                throw new DockerClientException(String.format(
                        "Dockerfile is excluded by pattern '%s' in .dockerignore file", matchingIgnorePattern));
            }

            Collection<File> filesInBuildContext = FileUtils.listFiles(baseDirectory, TrueFileFilter.INSTANCE,
                    TrueFileFilter.INSTANCE);

            for (File f : filesInBuildContext) {
                if (effectiveMatchingIgnorePattern(f) == null) {
                    filesToAdd.add(f);
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
                String goLangPattern = pattern.startsWith("!") ? pattern.substring(1) : pattern;
                lineNumber++;
                try {
                    if (GoLangFileMatch.match(goLangPattern, fileName)) {
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
         * Returns the matching ignore pattern for the given file or null if it should NOT be ignored. Exception rules like "!Dockerfile"
         * will be respected.
         */
        private String effectiveMatchingIgnorePattern(File file) {
            String relativeFilename = FilePathUtil.relativize(baseDirectory, file);

            List<String> matchingPattern = matchingIgnorePatterns(relativeFilename);

            if (matchingPattern.isEmpty()) {
                return null;
            }

            String lastMatchingPattern = matchingPattern.get(matchingPattern.size() - 1);

            return !lastMatchingPattern.startsWith("!") ? lastMatchingPattern : null;
         }
    }
}
