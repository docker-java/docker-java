package com.github.dockerjava.core.dockerfile;

import com.google.common.base.Function;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static com.google.common.collect.Collections2.transform;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class DockerfileAddMultipleFilesTest extends TestCase {

    private static final Logger log = LoggerFactory.getLogger(DockerfileAddMultipleFilesTest.class);
    private static final Function<File, String> TO_FILE_NAMES = new Function<File, String>() {
        @Override
        public String apply(File file) {
            return file.getName();
        }
    };

    @Test
    public void testAddMultipleFiles() throws IOException {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("testAddMultipleFiles").getFile());
        Dockerfile dockerfile = new Dockerfile(new File(baseDir, "Dockerfile"));
        Dockerfile.ScannedResult result = dockerfile.parse();
        Collection<String> filesToAdd = transform(result.filesToAdd, TO_FILE_NAMES);

        assertThat(filesToAdd, containsInAnyOrder("Dockerfile", "src1", "src2"));
    }
}
