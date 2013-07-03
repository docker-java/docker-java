package com.kpelykh.docker.client.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;

public class CompressArchiveUtil {

	public static File archiveTARFiles(File baseDir, String dirToArchive, String archiveNameWithOutExtension) {

		File tarFile = null;
		
		try {
			File[] list = (new File(baseDir, dirToArchive)).listFiles();
			tarFile = new File(FileUtils.getTempDirectoryPath(), archiveNameWithOutExtension + ".tar");
            tarFile.deleteOnExit();

			byte[] buf = new byte[1024];
			int len;
			
			{
				TarArchiveOutputStream tos = new TarArchiveOutputStream(new FileOutputStream(tarFile));
				tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
				for (File file : list) {
					TarArchiveEntry tarEntry = new TarArchiveEntry(file.getName());
					tarEntry.setSize(file.length());

					FileInputStream fin = new FileInputStream(file);
					BufferedInputStream in = new BufferedInputStream(fin);
					tos.putArchiveEntry(tarEntry);

					while ((len = in.read(buf)) != -1) {
						tos.write(buf, 0, len);
					}

					in.close();

					tos.closeArchiveEntry();

				}
				tos.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tarFile;
	}
}
