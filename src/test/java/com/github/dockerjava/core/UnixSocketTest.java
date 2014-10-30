package com.github.dockerjava.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;

@Test(groups = "unix")
public class UnixSocketTest {

	@Test
	public void testUnixSocketPing() {
		DockerClientConfig config = DockerClientConfig
				.createDefaultConfigBuilder()
				.withVersion("1.14")
				.withUri("unix://localhost/var/run/docker.sock").build();
		DockerClient docker = DockerClientBuilder.getInstance(config)
				.build();
		docker.pingCmd();
	}

	@Test
	public void testUnixSocketCat() throws IOException {
		String image = "test";
		String containerId = null;
		
		DockerClientConfig config = DockerClientConfig
				.createDefaultConfigBuilder()
				.withVersion("1.14")
				.withUri("unix://localhost/var/run/docker.sock").build();
		DockerClient docker = DockerClientBuilder.getInstance(config).build();
		try {

		// construct a tar archive
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String text = "Hello Docker!\n";
		TarArchiveOutputStream tarStream = new TarArchiveOutputStream(os);
		// add some files
		putToArchive(tarStream, "Dockerfile",
				"FROM busybox\nADD testdata /testdata\n".getBytes());
		putToArchive(tarStream, "testdata/hello1", text.getBytes());
		tarStream.close();
		
		ByteArrayInputStream tarIn = new ByteArrayInputStream(os.toByteArray());

		InputStream in = docker.buildImageCmd(tarIn).withTag(image).exec();
		readStream(System.out, in);

		CreateContainerResponse resp = docker.createContainerCmd("test")
				.withCmd("cat", "hello1")
				.withWorkingDir("/testdata")
				.exec();
		containerId = resp.getId();
		Assert.assertEquals(null, resp.getWarnings());

		docker.startContainerCmd(resp.getId()).exec();		
		
		int ex = docker.waitContainerCmd(resp.getId()).exec();
		Assert.assertEquals(ex, 0);

		StringBuilder sb = new StringBuilder();
		InputStream stream = docker.logContainerCmd(resp.getId())
				.withStdErr()
				.withStdOut()
				.exec();
		readStream(sb, stream);
		assertThat(sb.toString(), endsWith(text));
		} finally {
			if(null != containerId) {
				docker.removeContainerCmd(containerId).exec();				
			}
			docker.removeImageCmd(image).exec();
			
		}
	}

	private void readStream(Appendable sb, InputStream stream)
			throws IOException {
		LineIterator itr = IOUtils.lineIterator(
				stream, "UTF-8");
		
		while(itr.hasNext()) {
			sb.append(itr.next());
			sb.append("\n");
		}
	}

	private void putToArchive(TarArchiveOutputStream tarStream, String name,
			byte[] content) throws IOException {
		TarArchiveEntry dockerfile = new TarArchiveEntry(name);
		dockerfile.setSize(content.length);
		tarStream.putArchiveEntry(dockerfile);
		tarStream.write(content);
		tarStream.closeArchiveEntry();
	}
}
