package com.github.dockerjava.core;

import static com.github.dockerjava.core.FilePathUtil.relativize;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;

public class CompressArchiveUtil {

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
