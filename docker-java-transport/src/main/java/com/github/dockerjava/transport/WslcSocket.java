package com.github.dockerjava.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A {@link Socket} whose streams are backed by a child process that bridges stdin/stdout to the
 * Docker daemon socket inside a WSL Containers (wslc) virtual machine, via
 * {@code wslc system session run docker system dial-stdio}.
 * <p>
 * wslc exposes neither a Windows named pipe nor a TCP port on the host, so this stdio bridge (the
 * same mechanism used by {@code DOCKER_HOST=ssh://}) is the only host-visible channel to the daemon.
 * Supplying a {@link Socket} is sufficient: Apache HttpClient5 &mdash; including the hijacked
 * exec/attach/log streams &mdash; drives it like any other socket.
 * <p>
 * The executable is taken from the {@code wslc://} URL path if present (e.g. {@code wslc:///wslc.exe}),
 * otherwise from the {@code WSLC_EXECUTABLE} environment variable, otherwise {@code wslc.exe}.
 */
public class WslcSocket extends Socket {

    // java.util.logging keeps this transport module dependency-free (it uses no slf4j).
    private static final Logger LOGGER = Logger.getLogger(WslcSocket.class.getName());

    private final String executable;

    private volatile Process process;

    public WslcSocket(String dockerHostPath) {
        this.executable = resolveExecutable(dockerHostPath);
    }

    static String resolveExecutable(String dockerHostPath) {
        if (dockerHostPath == null || dockerHostPath.trim().isEmpty() || "/".equals(dockerHostPath)) {
            String fromEnv = System.getenv("WSLC_EXECUTABLE");
            return fromEnv != null && !fromEnv.trim().isEmpty() ? fromEnv : "wslc.exe";
        }
        // A leading slash comes from a URL like wslc:///wslc.exe -- strip it for the executable name.
        return dockerHostPath.startsWith("/") ? dockerHostPath.substring(1) : dockerHostPath;
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException {
        connect(endpoint, 0);
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        // The connect timeout is not applied to the bridge spawn: ProcessBuilder.start() returns
        // immediately and the daemon stream has no socket-level read timeout (see class javadoc).
        List<String> command = new ArrayList<>();
        command.add(executable);
        command.add("system");
        command.add("session");
        command.add("run");
        command.add("docker");
        command.add("system");
        command.add("dial-stdio");

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(false);
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new IOException("Failed to start wslc Docker bridge " + command + ": " + e.getMessage(), e);
        }

        // Drain the bridge's stderr so a full pipe buffer can never block the daemon stream, and log
        // it at FINE so connection failures (e.g. dial-stdio cannot reach the daemon) are diagnosable.
        final Process startedProcess = process;
        Thread stderrDrain = new Thread(() -> {
            byte[] buffer = new byte[1024];
            try (InputStream stderr = startedProcess.getErrorStream()) {
                int read;
                while ((read = stderr.read(buffer)) != -1) {
                    if (read > 0 && LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("wslc bridge stderr: " + new String(buffer, 0, read, StandardCharsets.UTF_8).trim());
                    }
                }
            } catch (IOException ignored) {
                // process gone
            }
        }, "wslc-bridge-stderr");
        stderrDrain.setDaemon(true);
        stderrDrain.start();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (process == null) {
            throw new IOException("Socket is not connected");
        }
        // stdout of the bridge process = bytes coming back from the Docker daemon socket
        return process.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (process == null) {
            throw new IOException("Socket is not connected");
        }
        // stdin of the bridge process = bytes written to the Docker daemon socket
        return process.getOutputStream();
    }

    @Override
    public void close() throws IOException {
        if (process != null) {
            process.destroyForcibly();
        }
    }
}
