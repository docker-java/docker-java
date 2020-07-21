package com.github.dockerjava.jsch;

import com.github.dockerjava.api.model.Container;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.OpenSSHConfig;
import com.jcraft.jsch.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This test relies on ~/.ssh/config which needs an entry for Host setup in the env variable DOCKER_HOST
 * <p>
 * config could look like:
 * <p>
 * <pre>
 * Host junit-host
 * HostName foo
 * StrictHostKeyChecking no
 * User bar
 * IdentityFile ~/.ssh/some_private_key
 * PreferredAuthentications publickey
 * </pre>
 */
@EnabledIfEnvironmentVariable(named = "DOCKER_HOST", matches = "ssh://.*")
class SocatHandlerIT {

    private static Session session;
    private Container container;

    @BeforeAll
    static void init() throws JSchException, IOException, URISyntaxException {
        final JSch jSch = new JSch();
        JSch.setLogger(new JschLogger());
        final String configFile = System.getProperty("user.home") + File.separator + ".ssh" + File.separator + "config";
        final File file = new File(configFile);
        if (file.exists()) {
            final OpenSSHConfig openSSHConfig = OpenSSHConfig.parseFile(file.getAbsolutePath());
            jSch.setConfigRepository(openSSHConfig);
        }
        session = jSch.getSession(new URI(System.getenv("DOCKER_HOST")).getHost());
        session.connect(500);
    }

    @AfterAll
    static void close() {
        session.disconnect();
    }

    @AfterEach
    void stopSocat() throws IOException, JSchException {
        if (container != null) {
            SocatHandler.stopSocat(session, container.getId());
        }
    }

    @org.junit.jupiter.api.Test
    @Timeout(value = 20)
    void startSocatAndPing() throws IOException, JSchException {
        container = SocatHandler.startSocat(session, "");
        assertNotNull(container);
        assertEquals("200", ping(container));
    }

    private String ping(Container container) throws JSchException, IOException {
        final Channel streamForwarder = session.getStreamForwarder(container.getPorts()[0].getIp(), container.getPorts()[0].getPublicPort());
        streamForwarder.connect(100);
        String cmd = "GET /_ping HTTP/1.0\r\n\r\n";
        final PrintWriter printWriter = new PrintWriter(streamForwarder.getOutputStream());
        printWriter.println(cmd);
        printWriter.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(streamForwarder.getInputStream()));
        for (String line; (line = reader.readLine()) != null; ) {
            final Matcher matcher = Pattern.compile("HTTP/\\d\\.\\d (\\d+) \\w+").matcher(line);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        fail("could not find response code");
        return null;
    }
}
