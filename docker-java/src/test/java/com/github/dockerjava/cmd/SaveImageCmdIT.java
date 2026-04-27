package com.github.dockerjava.cmd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerRule;
import com.github.dockerjava.core.RemoteApiVersion;
import lombok.SneakyThrows;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.dockerjava.utils.TestUtils.getVersion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class SaveImageCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(SaveImageCmdIT.class);

    @Test
    public void saveImage() throws Exception {

        try (
            InputStream inputStream = dockerRule.getClient().saveImageCmd("busybox").exec();
            InputStream image = IOUtils.toBufferedInputStream(inputStream)
        ) {
            assertThat(image.read(), not(-1));
        }

        try (
            InputStream inputStream = dockerRule.getClient().saveImageCmd("busybox").withTag("latest").exec();
            InputStream image2 = IOUtils.toBufferedInputStream(inputStream)
        ) {
            assertThat(image2.read(), not(-1));
        }

        ObjectMapper objectMapper = new ObjectMapper();

        if (getVersion(dockerRule.getClient()).isGreaterOrEqual(RemoteApiVersion.VERSION_1_48)) {

            try (DockerClient c = createDockerClient(DockerRule.config(null).withApiVersion(RemoteApiVersion.VERSION_1_48).build())) {

                c.pullImageCmd("busybox").withTag("latest").withPlatform("linux/arm64").start().awaitCompletion();

                Map<String, byte[]> tar;

                try (InputStream inputStream = c.saveImageCmd("busybox").withTag("latest").withPlatform("linux/arm64").exec()) {
                    tar = loadTar(inputStream);
                }

                List<Map<String, Object>> list =
                    objectMapper.readValue(tar.get("manifest.json"), new TypeReference<List<Map<String, Object>>>() {});

                assertThat(list.size(), is(1));

                Map<String, Object> config = objectMapper.readValue(tar.get(list.get(0).get("Config").toString()), new TypeReference<Map<String, Object>>() {});

                assertThat(config.get("architecture"), is("arm64"));
                assertThat(config.get("os"), is("linux"));

            }

        }

        // $TODO: test multi-platform save, but it requires containerd image store

    }

    @SneakyThrows
    private Map<String, byte[]> loadTar(InputStream data) {
        Map<String, byte[]> out = new LinkedHashMap<>();

        try (TarArchiveInputStream tin = new TarArchiveInputStream(data)) {
            TarArchiveEntry e;
            while ((e = tin.getNextEntry()) != null) {
                if (e.isDirectory()) {
                    continue;
                }
                String name = e.getName();
                long sizeL = e.getSize();
                if (sizeL < 0) {
                    throw new IOException("Negative size for tar entry: " + name + " size=" + sizeL);
                }
                if (sizeL > Integer.MAX_VALUE) {
                    throw new IOException("Tar entry too large to load into memory: " + name + " size=" + sizeL);
                }
                int size = (int) sizeL;

                // Read exactly this entry's bytes.
                byte[] bytes = readFully(tin, size);
                out.put(name, bytes);

                // TarArchiveInputStream aligns to 512-byte blocks internally; no manual skip needed.
            }
        }

        return out;

    }

    private static byte[] readFully(InputStream in, int size) throws IOException {
        byte[] buf = new byte[size];
        int off = 0;
        while (off < size) {
            int r = in.read(buf, off, size - off);
            if (r < 0) {
                throw new IOException("Unexpected EOF while reading tar entry (" + off + "/" + size + " bytes)");
            }
            off += r;
        }
        return buf;
    }


}
