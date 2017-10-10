package com.github.dockerjava.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

/**
 * @author Kanstantsin Shautsou
 */
public class TestUtils {
    public static final Logger LOG = LoggerFactory.getLogger(TestUtils.class);

    private TestUtils() {
    }

    public static RemoteApiVersion getVersion(DockerClient client) {
        final String serverVersion = client.versionCmd().exec().getApiVersion();
        return RemoteApiVersion.parseConfig(serverVersion);
    }

    public static boolean isSwarm(DockerClient client) {
        final String serverVersion = client.versionCmd().exec().getVersion();
        return serverVersion.startsWith("swarm/");
    }

    public static boolean isNotSwarm(DockerClient client) {
        return !isSwarm(client);
    }

    public static Network findNetwork(List<Network> networks, String name) {

        for (Network network : networks) {
            if (StringUtils.equals(network.getName(), name)) {
                return network;
            }
        }

        throw new AssertionError("No network found.");
    }

    public static String asString(InputStream response) {
        return consumeAsString(response);
    }

    public static String consumeAsString(InputStream response) {

        StringWriter logwriter = new StringWriter();

        try {
            LineIterator itr = IOUtils.lineIterator(response, "UTF-8");

            while (itr.hasNext()) {
                String line = itr.next();
                logwriter.write(line + (itr.hasNext() ? "\n" : ""));
                LOG.info("line: " + line);
            }
            response.close();

            return logwriter.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(response);
        }
    }
}
