package com.github.dockerjava.core.dockerfile;

import com.google.common.base.Function;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static com.google.common.collect.Collections2.transform;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class DockerfileAddMultipleFilesTest {

    private static final Function<File, String> TO_FILE_NAMES = File::getName;

    @Test
    public void ignoreAllBut() throws Exception {
        File baseDir = fileFromBuildTestResource("dockerignore/IgnoreAllBut");
        Dockerfile dockerfile = new Dockerfile(new File(baseDir, "Dockerfile"), baseDir);
        Dockerfile.ScannedResult result = dockerfile.parse();
        Collection<String> filesToAdd = transform(result.filesToAdd, TO_FILE_NAMES);

        assertThat(filesToAdd,
                   containsInAnyOrder("Dockerfile", "foo.jar"));
    }

    @Test
    public void nestedDirsPatterns() throws Exception {
        File baseDir = fileFromBuildTestResource("dockerignore/NestedDirsDockerignore");
        Dockerfile dockerfile = new Dockerfile(new File(baseDir, "Dockerfile"), baseDir);
        Dockerfile.ScannedResult result = dockerfile.parse();
        Collection<String> filesToAdd = transform(result.filesToAdd, TO_FILE_NAMES);

        assertThat(filesToAdd,
                containsInAnyOrder("Dockerfile", ".dockerignore", "README.md", "README-grand.md", "b.txt"));
    }

    @Test
    public void effectiveIgnorePatterns() throws Exception {
        File baseDir = fileFromBuildTestResource("dockerignore/EffectiveDockerignorePatterns");
        Dockerfile dockerfile = new Dockerfile(new File(baseDir, "Dockerfile"), baseDir);
        Dockerfile.ScannedResult result = dockerfile.parse();
        Collection<String> filesToAdd = transform(result.filesToAdd, TO_FILE_NAMES);

        assertThat(filesToAdd, containsInAnyOrder("Dockerfile", ".dockerignore", "README.md"));
    }

    @Test
    public void ineffectiveIgnorePattern() throws Exception {
        File baseDir = fileFromBuildTestResource("dockerignore/IneffectiveDockerignorePattern");
        Dockerfile dockerfile = new Dockerfile(new File(baseDir, "Dockerfile"), baseDir);
        Dockerfile.ScannedResult result = dockerfile.parse();
        Collection<String> filesToAdd = transform(result.filesToAdd, TO_FILE_NAMES);

        assertThat(filesToAdd, containsInAnyOrder("Dockerfile", ".dockerignore", "README.md", "README-secret.md"));
    }

    @Test
    public void addFiles() throws IOException {
        File baseDir = fileFromBuildTestResource("ADD/files");
        new File(baseDir, "emptydir").mkdir();
        Dockerfile dockerfile = new Dockerfile(new File(baseDir, "Dockerfile"), baseDir);
        Dockerfile.ScannedResult result = dockerfile.parse();
        Collection<String> filesToAdd = transform(result.filesToAdd, TO_FILE_NAMES);

        assertThat(filesToAdd, containsInAnyOrder("emptydir", "Dockerfile", "src1", "src2"));
    }

    @Test
    public void symlinkToFile() throws IOException {
        File baseDir = fileFromBuildTestResource("symlinks/symlinkToFile");
        Dockerfile dockerFile = new Dockerfile(new File(baseDir, "Dockerfile"), baseDir);
        Dockerfile.ScannedResult result = dockerFile.parse();
        Collection<String> filesToAdd = transform(result.filesToAdd, TO_FILE_NAMES);

        assertThat(filesToAdd, containsInAnyOrder("hello.txt", "someLink", "Dockerfile"));
    }

    @Test
    public void symlinkToDir() throws IOException {
        File baseDir = fileFromBuildTestResource("symlinks/symlinkToDir");
        String filesPath = Paths.get(baseDir.getAbsolutePath(), "fileSystem").toString();
        Path linkPath = Paths.get(filesPath, "someLink");
        if (Files.exists(linkPath))
            Files.delete(linkPath);
        Files.createSymbolicLink(linkPath, Paths.get(filesPath, "someDir"));
        Dockerfile dockerFile = new Dockerfile(new File(baseDir, "Dockerfile"), baseDir);
        Dockerfile.ScannedResult result = dockerFile.parse();
        Collection<String> filesToAdd = transform(result.filesToAdd, TO_FILE_NAMES);

        assertThat(filesToAdd, containsInAnyOrder("hello.txt", "someLink", "Dockerfile"));
    }

    private File fileFromBuildTestResource(String resource) {
        return new File(Thread.currentThread().getContextClassLoader()
                .getResource("buildTests/" + resource).getFile());
    }
}
