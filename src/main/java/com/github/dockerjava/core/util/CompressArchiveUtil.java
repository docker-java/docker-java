package com.github.dockerjava.core.util;

import static com.github.dockerjava.core.util.FilePathUtil.relativize;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FileUtils;

import com.google.common.io.ByteStreams;

public class CompressArchiveUtil {
    private CompressArchiveUtil() {
        // utility class
    }

    static void addFileToTar(TarArchiveOutputStream tarArchiveOutputStream, Path file, String entryName)
            throws IOException {
        if (Files.isSymbolicLink(file)) {
            TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(entryName, TarArchiveEntry.LF_SYMLINK);
            tarArchiveEntry.setLinkName(Files.readSymbolicLink(file).toString());
            tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        } else {
            TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) tarArchiveOutputStream.createArchiveEntry(file.toFile(),
                    entryName);
            if (file.toFile().canExecute()) {
                tarArchiveEntry.setMode(tarArchiveEntry.getMode() | 0755);
            }
            tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
            if (file.toFile().isFile()) {
                try (InputStream input = new BufferedInputStream(Files.newInputStream(file))) {
                    ByteStreams.copy(input, tarArchiveOutputStream);
                }
            }
        }
        tarArchiveOutputStream.closeArchiveEntry();
    }

    private static TarArchiveOutputStream buildTarStream(Path outputPath, boolean gZipped) throws IOException {
        OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(outputPath));
        if (gZipped) {
            outputStream = new GzipCompressorOutputStream(outputStream);
        }
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        return tarArchiveOutputStream;
    }

    /**
     * Recursively tar file
     *
     * @param inputPath
     *            file path can be directory
     * @param outputPath
     *            where to put the archived file
     * @param childrenOnly
     *            if inputPath is directory and if childrenOnly is true, the archive will contain all of its children, else the archive
     *            contains unique entry which is the inputPath itself
     * @param gZipped
     *            compress with gzip algorithm
     */
    public static void tar(Path inputPath, Path outputPath, boolean gZipped, boolean childrenOnly) throws IOException {
        if (!Files.exists(inputPath)) {
            throw new FileNotFoundException("File not found " + inputPath);
        }
        FileUtils.touch(outputPath.toFile());

        try (TarArchiveOutputStream tarArchiveOutputStream = buildTarStream(outputPath, gZipped)) {
            if (!Files.isDirectory(inputPath)) {
                addFileToTar(tarArchiveOutputStream, inputPath, inputPath.getFileName().toString());
            } else {
                Path sourcePath = inputPath;
                if (!childrenOnly) {
                    // In order to have the dossier as the root entry
                    sourcePath = inputPath.getParent();
                }
                Files.walkFileTree(inputPath, new TarDirWalker(sourcePath, tarArchiveOutputStream));
            }
            tarArchiveOutputStream.flush();
        }
    }

    public static File archiveTARFiles(File base, Iterable<File> files, String archiveNameWithOutExtension)
            throws IOException {
        File tarFile = new File(FileUtils.getTempDirectoryPath(), archiveNameWithOutExtension + ".tar");
        tarFile.deleteOnExit();
        try (TarArchiveOutputStream tos = new TarArchiveOutputStream(new GZIPOutputStream(new BufferedOutputStream(
                new FileOutputStream(tarFile))))) {
            tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            for (File file : files) {
                // relativize with method using Path otherwise method with File resolves the symlinks
                // and this is not want we want. If the file is a symlink, the relativized path should
                // keep the symlink name and not the target it points to.
                addFileToTar(tos, file.toPath(), relativize(base.toPath(), file.toPath()));
            }
        }

        return tarFile;
    }
}
