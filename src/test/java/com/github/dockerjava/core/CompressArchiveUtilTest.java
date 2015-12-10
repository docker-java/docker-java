package com.github.dockerjava.core;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.github.dockerjava.core.util.CompressArchiveUtil;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

public class CompressArchiveUtilTest {

    @Test
    public void testExecutableFlagIsPreserved() throws Exception {
        File executableFile = createExecutableFile();
        File archive = CompressArchiveUtil.archiveTARFiles(executableFile.getParentFile(), asList(executableFile),
                "archive");
        File expectedFile = extractFileByName(archive, "executableFile.sh.result");

        assertThat("should be executable", expectedFile.canExecute());
    }

    private File createExecutableFile() throws IOException {
        File baseDir = new File(FileUtils.getTempDirectoryPath());
        File executableFile = new File(baseDir, "executableFile.sh");
        executableFile.createNewFile();
        executableFile.setExecutable(true);
        assertThat(executableFile.canExecute(), is(true));
        return executableFile;
    }

    private File extractFileByName(File archive, String filenameToExtract) throws IOException {
        File baseDir = new File(FileUtils.getTempDirectoryPath());
        File expectedFile = new File(baseDir, filenameToExtract);
        expectedFile.delete();
        assertThat(expectedFile.exists(), is(false));

        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new GZIPInputStream(
                new BufferedInputStream(new FileInputStream(archive))));
        TarArchiveEntry entry;
        while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {
            String individualFiles = entry.getName();
            // there should be only one file in this archive
            assertThat(individualFiles, equalTo("executableFile.sh"));
            IOUtils.copy(tarArchiveInputStream, new FileOutputStream(expectedFile));
            if ((entry.getMode() & 0755) == 0755) {
                expectedFile.setExecutable(true);
            }
        }
        tarArchiveInputStream.close();
        return expectedFile;
    }
}
