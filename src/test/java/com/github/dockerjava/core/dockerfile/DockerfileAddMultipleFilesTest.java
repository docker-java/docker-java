package com.github.dockerjava.core.dockerfile;

import static com.google.common.collect.Collections2.transform;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.google.common.base.Function;

public class DockerfileAddMultipleFilesTest {

    private static final Logger log = LoggerFactory.getLogger(DockerfileAddMultipleFilesTest.class);

    private static final Function<File, String> TO_FILE_NAMES = new Function<File, String>() {
        @Override
        public String apply(File file) {
            return file.getName();
        }
    };

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
        Dockerfile dockerfile = new Dockerfile(new File(baseDir, "Dockerfile"), baseDir);
        Dockerfile.ScannedResult result = dockerfile.parse();
        Collection<String> filesToAdd = transform(result.filesToAdd, TO_FILE_NAMES);

        assertThat(filesToAdd, containsInAnyOrder("Dockerfile", "src1", "src2"));
    }

    private File fileFromBuildTestResource(String resource) {
        return new File(Thread.currentThread().getContextClassLoader()
                .getResource("buildTests/" + resource).getFile());
    }
}
