package com.github.dockerjava.core;

import static com.github.dockerjava.core.FilePathUtil.relativize;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;

public class CompressArchiveUtil {

    public static File archiveTARFiles(File base, Iterable<File> files, String archiveNameWithOutExtension)
            throws IOException {
        File tarFile = new File(FileUtils.getTempDirectoryPath(), archiveNameWithOutExtension + ".tar");
        tarFile.deleteOnExit();
        TarArchiveOutputStream tos = new TarArchiveOutputStream(new FileOutputStream(tarFile));
        try {
            tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            for (File file : files) {
                TarArchiveEntry tarEntry = new TarArchiveEntry(file);
                tarEntry.setName(relativize(base, file));

                if (!file.isDirectory()) {
                    if (file.canExecute()) {
                        tarEntry.setMode(tarEntry.getMode() | 0755);
                    }
                }

                tos.putArchiveEntry(tarEntry);

                if (!file.isDirectory()) {
                    FileUtils.copyFile(file, tos);
                }
                tos.closeArchiveEntry();
            }
        } finally {
            tos.close();
        }

        return tarFile;
    }
}
