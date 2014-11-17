package com.github.dockerjava.core;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class CompressArchiveUtil {

    public static File archiveTARFiles(File base, Iterable<File> files, String archiveNameWithOutExtension) throws IOException {
        File tarFile = new File(FileUtils.getTempDirectoryPath(), archiveNameWithOutExtension + ".tar");
        TarArchiveOutputStream tos = new TarArchiveOutputStream(new FileOutputStream(tarFile));
        try {
            tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            for (File file : files) {
                TarArchiveEntry tarEntry = new TarArchiveEntry(file);
                tarEntry.setName(relativize(base.getCanonicalFile(), file.getCanonicalFile()));

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

    public static String relativize(File base, File absolute) {
        String relative = base.toURI().relativize(absolute.toURI()).getPath();
        return relative;
    }
}
