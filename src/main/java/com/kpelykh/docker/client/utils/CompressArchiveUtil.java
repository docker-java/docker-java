package com.kpelykh.docker.client.utils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Collection;

import static org.apache.commons.io.filefilter.FileFilterUtils.*;

public class CompressArchiveUtil {

	public static File archiveTARFiles(File baseDir, String archiveNameWithOutExtension) throws IOException {

		File tarFile = null;
		
        tarFile = new File(FileUtils.getTempDirectoryPath(), archiveNameWithOutExtension + ".tar");

        Collection<File> files =
                FileUtils.listFiles(
                        baseDir,
                        new RegexFileFilter("^(.*?)"),
                        and(directoryFileFilter(), notFileFilter(nameFileFilter(baseDir.getName()))));

        byte[] buf = new byte[1024];
        int len;

        {
            TarArchiveOutputStream tos = new TarArchiveOutputStream(new FileOutputStream(tarFile));
            tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            for (File file : files) {
                TarArchiveEntry tarEntry = new TarArchiveEntry(file);
                tarEntry.setName(StringUtils.substringAfter(file.toString(), baseDir.getPath()));

                tos.putArchiveEntry(tarEntry);

                if (!file.isDirectory()) {
                    FileInputStream fin = new FileInputStream(file);
                    BufferedInputStream in = new BufferedInputStream(fin);

                    while ((len = in.read(buf)) != -1) {
                        tos.write(buf, 0, len);
                    }

                    in.close();
                }
                tos.closeArchiveEntry();

            }
            tos.close();
        }

		
		return tarFile;
	}
}
