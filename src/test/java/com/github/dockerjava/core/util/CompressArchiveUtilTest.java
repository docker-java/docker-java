package com.github.dockerjava.core.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CompressArchiveUtilTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testExecutableFlagIsPreserved() throws Exception {
        File executableFile = tempFolder.newFile("executableFile.sh");
        executableFile.setExecutable(true);
        assertThat(executableFile.canExecute(), is(true));

        File archive = CompressArchiveUtil.archiveTARFiles(tempFolder.getRoot(), asList(executableFile),
                "archive");
        File expectedFile = extractFileByName(archive, "executableFile.sh", "executableFile.sh.result");

        assertThat("should be executable", expectedFile.canExecute());
    }

    private File extractFileByName(File archive, String filenameToExtract, String outputName) throws IOException {
        File expectedFile = new File(tempFolder.newFolder(), outputName);
        expectedFile.delete();
        assertThat(expectedFile.exists(), is(false));

        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new GZIPInputStream(
                new BufferedInputStream(new FileInputStream(archive))));
        TarArchiveEntry entry;
        boolean found = false;
        while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {
            String individualFiles = entry.getName();
            if (individualFiles.equals(filenameToExtract) || individualFiles.endsWith("/" + filenameToExtract)) {
                found = true;
                IOUtils.copy(tarArchiveInputStream, new FileOutputStream(expectedFile));
                if ((entry.getMode() & 0755) == 0755) {
                    expectedFile.setExecutable(true);
                }
                break;
            }
        }
        assertThat("should extracted the file", found);
        tarArchiveInputStream.close();
        return expectedFile;
    }

    @Test
    public void testSymbolicLinkDir() throws IOException {
        Path uploadDir = tempFolder.newFolder("upload").toPath();
        Path linkTarget = tempFolder.newFolder("link-target").toPath();
        Path tmpFile = Files.createTempFile(linkTarget, "link-dir", "rand");
        Files.createSymbolicLink(uploadDir.resolve("link-folder"), linkTarget);
        Path tarGzFile = tempFolder.newFile("docker-java.tar.gz").toPath();
        //follow link only works for childrenOnly=false
        CompressArchiveUtil.tar(uploadDir, tarGzFile, true, false);
        File expectedFile = extractFileByName(tarGzFile.toFile(), tmpFile.toFile().getName(), "foo1");
        assertThat(expectedFile.canRead(), is(true));
    }

    @Test
    public void testSymbolicLinkFile() throws IOException {
        Path uploadDir = tempFolder.newFolder("upload").toPath();
        Path tmpFile = tempFolder.newFile("src").toPath();
        Files.createSymbolicLink(uploadDir.resolve("link-file"), tmpFile);
        Path tarGzFile = tempFolder.newFile("docker-java.tar.gz").toPath();
        boolean childrenOnly = false;
        CompressArchiveUtil.tar(uploadDir, tarGzFile, true, childrenOnly);
        File expectedFile = extractFileByName(tarGzFile.toFile(), "link-file", "foo1");
        assertThat(expectedFile.canRead(), is(true));
        childrenOnly = true;
        CompressArchiveUtil.tar(uploadDir, tarGzFile, true, childrenOnly);
        extractFileByName(tarGzFile.toFile(), "link-file", "foo1");
        assertThat(expectedFile.canRead(), is(true));
    }
}
