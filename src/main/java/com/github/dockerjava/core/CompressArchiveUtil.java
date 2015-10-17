package com.github.dockerjava.core;

import static com.github.dockerjava.core.FilePathUtil.relativize;

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
import com.google.common.io.Closeables;

public class CompressArchiveUtil {

    static void putTarEntry(TarArchiveOutputStream tarOutputStream, TarArchiveEntry tarEntry, Path file) throws IOException {
        tarEntry.setSize(Files.size(file));
        tarOutputStream.putArchiveEntry(tarEntry);
        InputStream input = new BufferedInputStream(Files.newInputStream(file));
        try {
            ByteStreams.copy(input, tarOutputStream);
            tarOutputStream.closeArchiveEntry();
        } finally {
            input.close();
        }
    }

    /**
     * Recursively tar file
     *
     * @param inputPath    file path can be directory
     * @param outputPath   where to put the archived file
     * @param childrenOnly if inputPath is directory and if childrenOnly is true, the archive will contain all of its children, else the archive contains unique
     *                     entry which is the inputPath itself
     * @param gZipped      compress with gzip algorithm
     */
    public static void tar(Path inputPath, Path outputPath, boolean gZipped, boolean childrenOnly) throws IOException {
        if (!Files.exists(inputPath)) {
            throw new FileNotFoundException("File not found " + inputPath);
        }
        FileUtils.touch(outputPath.toFile());
        OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(outputPath));
        if (gZipped) {
            outputStream = new GzipCompressorOutputStream(outputStream);
        }
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        try {
            if (!Files.isDirectory(inputPath)) {
                putTarEntry(tarArchiveOutputStream, new TarArchiveEntry(inputPath.getFileName().toString()), inputPath);
            } else {
                Path sourcePath = inputPath;
                if (!childrenOnly) {
                    // In order to have the dossier as the root entry
                    sourcePath = inputPath.getParent();
                }
                Files.walkFileTree(inputPath, new TarDirWalker(sourcePath, tarArchiveOutputStream));
            }
            tarArchiveOutputStream.flush();
        } finally {
            Closeables.close(tarArchiveOutputStream, true);
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
                TarArchiveEntry tarEntry = new TarArchiveEntry(file);
                tarEntry.setName(relativize(base, file));

                if (!file.isDirectory() && file.canExecute()) {
                    tarEntry.setMode(tarEntry.getMode() | 0755);
                }

                tos.putArchiveEntry(tarEntry);

                if (!file.isDirectory()) {
                    FileUtils.copyFile(file, tos);
                }
                tos.closeArchiveEntry();
            }
        }

        return tarFile;
    }
}
