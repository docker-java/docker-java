package com.github.dockerjava.jsch;

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerPort;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

public class SocatHandler {

    public static final int INTERNAL_SOCAT_PORT = 2377;
    private static Logger logger = LoggerFactory.getLogger(SocatHandler.class);

    private SocatHandler() {
    }

    public static Container startSocat(Session session) throws JSchException, IOException {

        final String command = " docker run -d " +
            " -p 127.0.0.1:0:" + INTERNAL_SOCAT_PORT +
            " -v /var/run/docker.sock:/var/run/docker.sock " +
            "  alpine/socat " +
            "  tcp-listen:" + INTERNAL_SOCAT_PORT + ",fork,reuseaddr unix-connect:/var/run/docker.sock";

        final String containerId = runCommand(session, command);

        try {
            final Container container = new Container();
            final Field id;
            id = container.getClass().getDeclaredField("id");
            id.setAccessible(true);
            id.set(container, containerId);

            /*
            final String inspectionCommand = String.format("docker inspect " +
                    "--format=\"{{(index (index .NetworkSettings.Ports \\\"" + INTERNAL_SOCAT_PORT + "/tcp\\\") 0).HostPort}}\" %s",
                containerId);
            final String publishedPort = runCommand(session, inspectionCommand);
            */

            String portCommand = String.format("docker port %s", containerId);
            final String portResult = runCommand(session, portCommand).trim();
            final String publishedPort = portResult.substring(portResult.lastIndexOf(':') + 1);

            final Field ports = container.getClass().getField("ports");
            final ContainerPort containerPort = new ContainerPort()
                .withIp("127.0.0.1")
                .withPrivatePort(INTERNAL_SOCAT_PORT)
                .withPublicPort(Integer.valueOf(publishedPort.trim()));
            ports.set(container, new ContainerPort[]{containerPort});
            return container;

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private static String runCommand(Session session, String command) throws JSchException, IOException {

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        try {
            channel.setCommand(command);
            logger.debug("running command: {}", command);

            final InputStream in = channel.getInputStream();
            final InputStream errStream = channel.getErrStream();

            channel.connect();

            while (true) {

                if (channel.isClosed()) {

                    String response = null;
                    String errorMessage = null;

                    while (in.available() > 0) {
                        byte[] tmp = new byte[1024];
                        int i = in.read(tmp, 0, 1024);
                        if (i < 0) break;
                        response = new String(tmp, 0, i, Charset.defaultCharset());
                    }

                    while (errStream.available() > 0) {
                        byte[] tmp = new byte[1024];
                        int i = errStream.read(tmp, 0, 1024);
                        if (i < 0) break;
                        errorMessage = new String(tmp, Charset.defaultCharset());
                    }

                    logger.debug("exit-status: {}", channel.getExitStatus());
                    logger.debug("stderr:{}", errorMessage);
                    logger.debug("stdout: {}", response);

                    if (channel.getExitStatus() == 0) {
                        return response;
                    } else {
                        throw new RuntimeException("command ended in exit-status:" + channel.getExitStatus() +
                            " with error message: " + errorMessage);
                    }
                }
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            channel.disconnect();
        }
    }

    public static void stopSocat(Session session, String containerId) throws JSchException, IOException {
        final String command = " docker stop " + containerId;
        runCommand(session, command);
    }
}
