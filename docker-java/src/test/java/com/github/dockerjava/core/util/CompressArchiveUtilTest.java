package com.github.dockerjava.core.util;

import com.google.common.collect.Lists;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CompressArchiveUtilTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void tarWithRegularFileAsInput() throws Exception {
        Path archiveSourceFile = tempFolder.getRoot().toPath().resolve("sourceFile");
        createFileWithContent(archiveSourceFile);

        // ChildrenOnly = false
        Path tarGzFile = tempFolder.newFile("archive.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceFile, tarGzFile, true, false);
        assertEquals(1, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsNonEmptyFile(tarGzFile.toFile(), "sourceFile");

        // ChildrenOnly = true, this option make no sense when input is a file but still, let's test it
        // to make sure it behaves as expected
        tarGzFile = tempFolder.newFile("archiveChildrenOnly.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceFile, tarGzFile, true, false);
        assertEquals(1, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsNonEmptyFile(tarGzFile.toFile(), "sourceFile");
    }

    @Test
    public void tarWithExecutableFileAsInput() throws Exception {
        Path archiveSourceFile = tempFolder.getRoot().toPath().resolve("executableFile.sh");
        createFileWithContent(archiveSourceFile);
        archiveSourceFile.toFile().setExecutable(true);

        // ChildrenOnly = false
        Path tarGzFile = tempFolder.newFile("archive.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceFile, tarGzFile, true, false);
        assertEquals(1, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsExecutableFile(tarGzFile.toFile(), "executableFile.sh");

        // ChildrenOnly = true, this option make no sense when input is a file but still, let's test it
        // to make sure it behaves as expected
        tarGzFile = tempFolder.newFile("archiveChildrenOnly.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceFile, tarGzFile, true, false);
        assertEquals(1, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsExecutableFile(tarGzFile.toFile(), "executableFile.sh");
    }

    @Test
    public void tarWithSymbolicLinkFileAsInput() throws IOException {
        Path archiveSourceFile = tempFolder.getRoot().toPath().resolve("symlinkFile");
        Path linkTargetFile = tempFolder.newFile("link-target").toPath();
        Files.createSymbolicLink(archiveSourceFile, linkTargetFile);

        // ChildrenOnly = false
        Path tarGzFile = tempFolder.newFile("archive.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceFile, tarGzFile, true, false);
        assertEquals(1, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsSymlink(tarGzFile.toFile(), "symlinkFile", linkTargetFile.toString());

        // ChildrenOnly = true, this option make no sense when input is a file but still, let's test it
        // to make sure it behaves as expected
        tarGzFile = tempFolder.newFile("archiveChildrenOnly.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceFile, tarGzFile, true, false);
        assertEquals(1, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsSymlink(tarGzFile.toFile(), "symlinkFile", linkTargetFile.toString());
    }

    @Test
    public void tarWithfolderAsInput() throws Exception {
        Path archiveSourceDir = tempFolder.newFolder("archive-source").toPath();
        createFoldersAndSubFolderWithFiles(archiveSourceDir);

        // ChildrenOnly = false
        Path tarGzFile = tempFolder.newFile("archive.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceDir, tarGzFile, true, false);
        assertEquals(7, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsDirectory(tarGzFile.toFile(), "archive-source");
        assertTarArchiveEntryIsDirectory(tarGzFile.toFile(), "folderA");
        assertTarArchiveEntryIsDirectory(tarGzFile.toFile(), "folderB");
        assertTarArchiveEntryIsDirectory(tarGzFile.toFile(), "subFolderB");
        assertTarArchiveEntryIsNonEmptyFile(tarGzFile.toFile(), "fileA");
        assertTarArchiveEntryIsNonEmptyFile(tarGzFile.toFile(), "fileB");
        assertTarArchiveEntryIsNonEmptyFile(tarGzFile.toFile(), "subFileB");

        // ChildrenOnly = true
        tarGzFile = tempFolder.newFile("archiveChildrenOnly.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceDir, tarGzFile, true, true);
        assertEquals(6, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsDirectory(tarGzFile.toFile(), "folderA");
        assertTarArchiveEntryIsDirectory(tarGzFile.toFile(), "folderB");
        assertTarArchiveEntryIsDirectory(tarGzFile.toFile(), "subFolderB");
        assertTarArchiveEntryIsNonEmptyFile(tarGzFile.toFile(), "fileA");
        assertTarArchiveEntryIsNonEmptyFile(tarGzFile.toFile(), "fileB");
        assertTarArchiveEntryIsNonEmptyFile(tarGzFile.toFile(), "subFileB");
    }

    @Test
    public void tarWithfolderAsInputAndNestedExecutableFile() throws Exception {
        Path archiveSourceDir = tempFolder.newFolder("archive-source").toPath();
        Path executableFile = archiveSourceDir.resolve("executableFile.sh");
        createFileWithContent(executableFile);
        executableFile.toFile().setExecutable(true);

        // ChildrenOnly = false
        Path tarGzFile = tempFolder.newFile("archive.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceDir, tarGzFile, true, false);
        assertEquals(2, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsDirectory(tarGzFile.toFile(), "archive-source");
        assertTarArchiveEntryIsExecutableFile(tarGzFile.toFile(), "executableFile.sh");

        // ChildrenOnly = true
        tarGzFile = tempFolder.newFile("archiveChildrenOnly.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceDir, tarGzFile, true, true);
        assertEquals(1, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsExecutableFile(tarGzFile.toFile(), "executableFile.sh");
    }

    @Test
    public void tarWithfolderAsInputAndNestedSymbolicLinkFile() throws Exception {
        Path archiveSourceDir = tempFolder.newFolder("archive-source").toPath();
        Path linkTargetFile = tempFolder.newFile("link-target").toPath();
        Path symlinkFile = archiveSourceDir.resolve("symlinkFile");
        Files.createSymbolicLink(symlinkFile, linkTargetFile);

        // ChildrenOnly = false
        Path tarGzFile = tempFolder.newFile("archive.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceDir, tarGzFile, true, false);
        assertEquals(2, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsDirectory(tarGzFile.toFile(), "archive-source");
        assertTarArchiveEntryIsSymlink(tarGzFile.toFile(), "symlinkFile", linkTargetFile.toString());

        // ChildrenOnly = true
        tarGzFile = tempFolder.newFile("archiveChildrenOnly.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceDir, tarGzFile, true, true);
        assertEquals(1, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsSymlink(tarGzFile.toFile(), "symlinkFile", linkTargetFile.toString());
    }

    @Test
    public void tarWithfolderAsInputAndNestedSymbolicLinkDir() throws Exception {
        Path archiveSourceDir = tempFolder.newFolder("archive-source").toPath();
        Path linkTargetDir = tempFolder.newFolder("link-target").toPath();
        Path symlinkFile = archiveSourceDir.resolve("symlinkFile");
        Files.createSymbolicLink(symlinkFile, linkTargetDir);

        // ChildrenOnly = false
        Path tarGzFile = tempFolder.newFile("archive.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceDir, tarGzFile, true, false);
        assertEquals(2, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsDirectory(tarGzFile.toFile(), "archive-source");
        assertTarArchiveEntryIsSymlink(tarGzFile.toFile(), "symlinkFile", linkTargetDir.toString());

        // ChildrenOnly = true
        tarGzFile = tempFolder.newFile("archiveChildrenOnly.tar.gz").toPath();
        CompressArchiveUtil.tar(archiveSourceDir, tarGzFile, true, true);
        assertEquals(1, getNumberOfEntryInArchive(tarGzFile.toFile()));
        assertTarArchiveEntryIsSymlink(tarGzFile.toFile(), "symlinkFile", linkTargetDir.toString());
    }

    @Test
    public void archiveTARFilesWithFolderAndFiles() throws Exception {
        File archive = CompressArchiveUtil.archiveTARFiles(tempFolder.getRoot(),
                createFoldersAndSubFolderWithFiles(tempFolder.getRoot().toPath()), "archive");
        assertEquals(6, getNumberOfEntryInArchive(archive));
        assertTarArchiveEntryIsDirectory(archive, "folderA");
        assertTarArchiveEntryIsDirectory(archive, "folderB");
        assertTarArchiveEntryIsDirectory(archive, "subFolderB");
        assertTarArchiveEntryIsNonEmptyFile(archive, "fileA");
        assertTarArchiveEntryIsNonEmptyFile(archive, "fileB");
        assertTarArchiveEntryIsNonEmptyFile(archive, "subFileB");
    }

    @Test
    public void archiveTARFilesWithExecutableFile() throws Exception {
        File executableFile = tempFolder.newFile("executableFile.sh");
        executableFile.setExecutable(true);

        File archive = CompressArchiveUtil.archiveTARFiles(tempFolder.getRoot(), asList(executableFile), "archive");
        assertEquals(1, getNumberOfEntryInArchive(archive));
        assertTarArchiveEntryIsExecutableFile(archive, "executableFile.sh");
    }

    @Test
    public void archiveTARFilesWithSymbolicLinkFile() throws Exception {
        Path linkTargetFile = tempFolder.newFile("link-target").toPath();
        Path symlinkFile = tempFolder.getRoot().toPath().resolve("symlinkFile");
        Files.createSymbolicLink(symlinkFile, linkTargetFile);

        File archive = CompressArchiveUtil.archiveTARFiles(tempFolder.getRoot(), asList(symlinkFile.toFile()), "archive");
        assertEquals(1, getNumberOfEntryInArchive(archive));
        assertTarArchiveEntryIsSymlink(archive, "symlinkFile", linkTargetFile.toString());
    }

    @Test
    public void archiveTARFilesWithSymbolicLinkDir() throws Exception {
        Path linkTargetDir = tempFolder.newFolder("link-target").toPath();
        Path symlinkFile = tempFolder.getRoot().toPath().resolve("symlinkFile");
        Files.createSymbolicLink(symlinkFile, linkTargetDir);

        File archive = CompressArchiveUtil.archiveTARFiles(tempFolder.getRoot(), asList(symlinkFile.toFile()), "archive");
        assertEquals(1, getNumberOfEntryInArchive(archive));
        assertTarArchiveEntryIsSymlink(archive, "symlinkFile", linkTargetDir.toString());
    }

    @Test
    public void mergeTarStreams() throws Exception {
        final Path basePathIn = Files.createDirectories(tempFolder.getRoot().toPath().resolve("merge-tar-streams").resolve("in"));
        final Path basePathOut = Files.createDirectories(tempFolder.getRoot().toPath().resolve("merge-tar-streams").resolve("out"));
        File resultArchive = basePathOut.resolve("result-archive.tar").toFile();

        final File file1 = createFileWithContent(basePathIn.resolve("fileA"));
        final File file2 = createFileWithContent(basePathIn.resolve("fileB"));

        File archive1 = CompressArchiveUtil.archiveTARFiles(file1.getParentFile(), Collections.singletonList(file1), "archive1");
        File archive2 = CompressArchiveUtil.archiveTARFiles(file2.getParentFile(), Collections.singletonList(file2), "archive2");

        try (InputStream stream1 = new BufferedInputStream(new FileInputStream(archive1));
             InputStream stream2 = new BufferedInputStream(new FileInputStream(archive2))) {
            try (InputStream mergedStream = CompressArchiveUtil.mergeTarStreams(Lists.newArrayList(stream1, stream2));
                 OutputStream outStream = new BufferedOutputStream(new FileOutputStream(resultArchive))) {
                IOUtils.copy(mergedStream, outStream);
            }
        }

        assertEquals(2, getNumberOfEntryInArchive(resultArchive));
        assertTarArchiveEntryIsNonEmptyFile(resultArchive, "fileA");
        assertTarArchiveEntryIsNonEmptyFile(resultArchive, "fileB");
    }

    @Test
    public void mergeTarStreamsSameFileNameOverrideLastWhenExtracted() throws Exception {
        final Path basePathIn1 = Files.createDirectories(tempFolder.getRoot().toPath()
                .resolve("merge-tar-streams-override").resolve("file1"));
        final Path basePathIn2 = Files.createDirectories(tempFolder.getRoot().toPath()
                .resolve("merge-tar-streams-override").resolve("file2"));
        final Path basePathOut = Files.createDirectories(tempFolder.getRoot().toPath().resolve("merge-tar-streams-override"));
        File resultArchive = basePathOut.resolve("result-archive.tar").toFile();

        final File file1 = createFileWithContent(basePathIn1.resolve("fileA"), "contentA");
        final File file2 = createFileWithContent(basePathIn2.resolve("fileA"), "contentB");

        File archive1 = CompressArchiveUtil.archiveTARFiles(file1.getParentFile(), Collections.singletonList(file1), "archive1");
        File archive2 = CompressArchiveUtil.archiveTARFiles(file2.getParentFile(), Collections.singletonList(file2), "archive2");

        try (InputStream stream1 = new BufferedInputStream(new FileInputStream(archive1));
             InputStream stream2 = new BufferedInputStream(new FileInputStream(archive2))) {
            try (InputStream mergedStream = CompressArchiveUtil.mergeTarStreams(Lists.newArrayList(stream1, stream2));
                 OutputStream outStream = new BufferedOutputStream(new FileOutputStream(resultArchive))) {
                IOUtils.copy(mergedStream, outStream);
            }
        }

        final List<TarArchiveEntry> entries = getTarArchiveEntries(resultArchive, "fileA");
        assertEquals(2, entries.size());

        final Path extractPath = Files.createDirectories(tempFolder.getRoot().toPath().resolve("merge-tar-streams-extract"));
        try (ArchiveInputStream ais = new TarArchiveInputStream(
                new GZIPInputStream(new BufferedInputStream(new FileInputStream(resultArchive))))) {
            ArchiveEntry nextEntry;
            while ((nextEntry = ais.getNextEntry()) != null) {
                try (OutputStream os = new BufferedOutputStream(new FileOutputStream(extractPath.resolve(nextEntry.getName()).toFile()))) {
                    IOUtils.copy(ais, os);
                }
            }
        }

        final Path extractedFile = extractPath.resolve("fileA");
        assertTrue(extractedFile.toFile().exists());
        assertTrue(extractedFile.toFile().isFile());
        final byte[] content = Files.readAllBytes(extractedFile);
        assertEquals("contentB", new String(content, StandardCharsets.UTF_8));
    }

    private static void assertTarArchiveEntryIsDirectory(File archive, String directoryName) throws IOException {
        TarArchiveEntry tarArchiveEntry = getTarArchiveEntry(archive, directoryName);
        assertNotNull(tarArchiveEntry);
        assertTrue(tarArchiveEntry.isDirectory());
    }

    private static void assertTarArchiveEntryIsNonEmptyFile(File archive, String fileName) throws IOException {
        TarArchiveEntry tarArchiveEntry = getTarArchiveEntry(archive, fileName);
        assertNotNull(tarArchiveEntry);
        assertTrue(tarArchiveEntry.isFile());
        assertTrue(tarArchiveEntry.getSize() > 0);
    }

    private static void assertTarArchiveEntryIsExecutableFile(File archive, String fileName) throws IOException {
        TarArchiveEntry tarArchiveEntry = getTarArchiveEntry(archive, fileName);
        assertNotNull(tarArchiveEntry);
        assertTrue(tarArchiveEntry.isFile());
        assertEquals("should be executable", (tarArchiveEntry.getMode() & 0755), 0755);
    }

    private static void assertTarArchiveEntryIsSymlink(File archive, String fileName, String expectedTarget) throws IOException {
        TarArchiveEntry tarArchiveEntry = getTarArchiveEntry(archive, fileName);
        assertNotNull(tarArchiveEntry);
        assertTrue("should be a symbolic link", tarArchiveEntry.isSymbolicLink());
        assertEquals(expectedTarget, tarArchiveEntry.getLinkName());
    }

    /**
     * Creates the following directory structure with files in the specified
     * destination folder
     *
     * <pre>
     * destinationFolder
     * |__folderA
     * |  |__fileA
     * |__folderB
     *    |__fileB
     *    |__subFolderB
     *       |__subFileB
     * </pre>
     *
     * @param destinationFolder where to create the folder/files.
     * @return the list of created files.
     * @throws IOException if an error occurs while creating the folders/files.
     */
    private static List<File> createFoldersAndSubFolderWithFiles(Path destinationFolder) throws IOException {
        List<File> createdFiles = new ArrayList<>();
        Path folderA = destinationFolder.resolve("folderA");
        createdFiles.add(Files.createDirectories(folderA).toFile());
        createdFiles.add(createFileWithContent(folderA.resolve("fileA")));

        Path folderB = destinationFolder.resolve("folderB");
        createdFiles.add(Files.createDirectories(folderB).toFile());
        createdFiles.add(createFileWithContent(folderB.resolve("fileB")));

        Path subFolderB = folderB.resolve("subFolderB");
        createdFiles.add(Files.createDirectories(subFolderB).toFile());
        createdFiles.add(createFileWithContent(folderA.resolve("subFileB")));
        return createdFiles;
    }

    private static File createFileWithContent(Path fileToCreate) throws IOException {
        return createFileWithContent(fileToCreate, "some content");
    }

    private static File createFileWithContent(Path fileToCreate, String content) throws IOException {
        try (InputStream in = new ByteArrayInputStream(content.getBytes())) {
            Files.copy(in, fileToCreate);
        }
        return fileToCreate.toFile();
    }

    private static TarArchiveEntry getTarArchiveEntry(File tarArchive, String filename) throws IOException {
        try (TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(
                new GZIPInputStream(new BufferedInputStream(new FileInputStream(tarArchive))))) {
            TarArchiveEntry entry;
            while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {
                if (entryNameEqualsToFilename(entry, filename)) {
                    return entry;
                }
            }
        }
        return null;
    }

    private static List<TarArchiveEntry> getTarArchiveEntries(File tarArchive, String filename) throws IOException {
        List<TarArchiveEntry> result = new ArrayList<>();
        try (TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(
                new GZIPInputStream(new BufferedInputStream(new FileInputStream(tarArchive))))) {
            TarArchiveEntry entry;
            while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {
                if (entryNameEqualsToFilename(entry, filename)) {
                    result.add(entry);
                }
            }
        }
        return result;
    }

    private static boolean entryNameEqualsToFilename(TarArchiveEntry entry, String filename) {
        return entry.getName().equals(filename)
                || entry.getName().endsWith("/" + filename)
                || entry.getName().equals(filename + "/")
                || entry.getName().endsWith("/" + filename + "/");
    }

    private static int getNumberOfEntryInArchive(File tarArchive) throws IOException {
        int numberOfEntries = 0;
        try (TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(
                new GZIPInputStream(new BufferedInputStream(new FileInputStream(tarArchive))))) {
            while ((tarArchiveInputStream.getNextTarEntry()) != null) {
                numberOfEntries++;
            }
        }
        return numberOfEntries;
    }
}
