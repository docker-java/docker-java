package com.github.dockerjava.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.transport.DockerHttpClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link DockerHttpClient} decorator that makes container port publishing, host bind mounts and
 * user-defined networks actually work on WSL Containers (wslc).
 * <p>
 * On wslc the Windows integration (VirtioNet port relay to {@code 127.0.0.1} and VirtioFs host-path
 * bind mounts) is wired by the wslc control plane <em>only</em> when a container is created and
 * started through the {@code wslc} CLI. A container created/started through the Docker Engine API
 * (the dial-stdio bridge) reaches the very same dockerd, but the relay/bind are never established.
 * <p>
 * This decorator therefore intercepts the four calls that must be reconciled with the wslc control
 * plane, translating each to the equivalent {@code wslc} command, and delegates everything else
 * (exec, logs, wait, stop, pull, build, ...) unchanged to the wrapped Docker-API client, which talks
 * to the same daemon:
 * <ul>
 *   <li>{@code POST /containers/create} &rarr; {@code wslc create}</li>
 *   <li>{@code POST /containers/{id}/start} &rarr; {@code wslc start} (this wires the relay/bind)</li>
 *   <li>{@code POST /networks/create} &rarr; {@code wslc network create} (so the network is visible
 *       to {@code wslc create --network})</li>
 *   <li>{@code GET /containers/{id}/json} &rarr; the daemon's inspect with {@code NetworkSettings.Ports}
 *       overridden from {@code wslc list} (the relay's real host port)</li>
 * </ul>
 * Because it sits at the {@link DockerHttpClient} layer, docker-java (and Testcontainers on top of it)
 * needs no other change.
 */
public class WslcLifecycleDockerHttpClient implements DockerHttpClient {

    private static final java.util.logging.Logger LOGGER =
        java.util.logging.Logger.getLogger(WslcLifecycleDockerHttpClient.class.getName());

    private static final Pattern CREATE = Pattern.compile(".*/containers/create$");
    private static final Pattern START = Pattern.compile(".*/containers/([^/]+)/start$");
    private static final Pattern NETWORK_CREATE = Pattern.compile(".*/networks/create$");
    private static final Pattern INSPECT = Pattern.compile(".*/containers/([^/]+)/json$");

    private final DockerHttpClient delegate;
    private final String wslc;
    private final ObjectMapper mapper = new ObjectMapper();

    public WslcLifecycleDockerHttpClient(DockerHttpClient delegate, String wslcExecutable) {
        this.delegate = delegate;
        this.wslc = (wslcExecutable == null || wslcExecutable.isEmpty()) ? "wslc.exe" : wslcExecutable;
    }

    @Override
    public Response execute(Request request) {
        String path = request.path();
        String noQuery = path.contains("?") ? path.substring(0, path.indexOf('?')) : path;
        if ("POST".equals(request.method())) {
            if (CREATE.matcher(noQuery).matches()) {
                return handleCreate(request);
            }
            if (NETWORK_CREATE.matcher(noQuery).matches()) {
                return handleNetworkCreate(request);
            }
            Matcher m = START.matcher(noQuery);
            if (m.matches()) {
                return handleStart(urlDecode(m.group(1)));
            }
        }
        if ("GET".equals(request.method()) && INSPECT.matcher(noQuery).matches()) {
            return handleInspect(request);
        }
        return delegate.execute(request);
    }

    // ---- POST /containers/create -> wslc create ----
    private Response handleCreate(Request request) {
        try {
            JsonNode body = mapper.readTree(readBody(request));
            JsonNode hostConfig = body.path("HostConfig");
            List<String> cmd = new ArrayList<>();
            cmd.add(wslc);
            cmd.add("create");

            String name = queryParam(request.path(), "name");
            addOpt(cmd, "--name", name);
            addOpt(cmd, "-h", text(body, "Hostname"));
            addOpt(cmd, "--domainname", text(body, "Domainname"));
            addOpt(cmd, "-u", text(body, "User"));
            addOpt(cmd, "-w", text(body, "WorkingDir"));
            addOpt(cmd, "--stop-signal", text(body, "StopSignal"));

            for (JsonNode env : body.path("Env")) {
                add(cmd, "-e", env.asText());
            }
            for (Iterator<Map.Entry<String, JsonNode>> it = body.path("Labels").fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> e = it.next();
                add(cmd, "-l", e.getKey() + "=" + e.getValue().asText());
            }
            // -p host:container from HostConfig.PortBindings {"80/tcp":[{"HostPort":"18086"}]}
            for (Iterator<Map.Entry<String, JsonNode>> it = hostConfig.path("PortBindings").fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> e = it.next();
                String containerPort = e.getKey().split("/")[0];
                String hostPort = e.getValue().isArray() && e.getValue().size() > 0
                    ? e.getValue().get(0).path("HostPort").asText("") : "";
                add(cmd, "-p", hostPort.isEmpty() ? containerPort : hostPort + ":" + containerPort);
            }
            if (hostConfig.path("PublishAllPorts").asBoolean(false)) {
                cmd.add("-P");
            }
            // -v host:container[:mode] from HostConfig.Binds ["C:\\dir:/mnt/x:rw"]
            for (JsonNode bind : hostConfig.path("Binds")) {
                add(cmd, "-v", bind.asText());
            }
            for (Iterator<Map.Entry<String, JsonNode>> it = hostConfig.path("Tmpfs").fields(); it.hasNext(); ) {
                add(cmd, "--tmpfs", it.next().getKey());
            }
            for (JsonNode dns : hostConfig.path("Dns")) {
                add(cmd, "--dns", dns.asText());
            }
            for (JsonNode u : hostConfig.path("Ulimits")) {
                add(cmd, "--ulimit", u.path("Name").asText() + "=" + u.path("Soft").asLong() + ":" + u.path("Hard").asLong());
            }
            long memory = hostConfig.path("Memory").asLong(0);
            if (memory > 0) {
                add(cmd, "-m", Long.toString(memory));
            }
            long nanoCpus = hostConfig.path("NanoCpus").asLong(0);
            if (nanoCpus > 0) {
                add(cmd, "--cpus", String.valueOf(nanoCpus / 1_000_000_000.0));
            }
            long shmSize = hostConfig.path("ShmSize").asLong(0);
            if (shmSize > 0) {
                add(cmd, "--shm-size", Long.toString(shmSize));
            }

            // Network: prefer the explicit NetworkingConfig endpoint (carries aliases), else NetworkMode.
            String network = null;
            JsonNode endpoints = body.path("NetworkingConfig").path("EndpointsConfig");
            if (endpoints.isObject() && endpoints.size() > 0) {
                network = endpoints.fieldNames().next();
                for (JsonNode alias : endpoints.path(network).path("Aliases")) {
                    add(cmd, "--network-alias", alias.asText());
                }
            }
            if (network == null) {
                String nm = hostConfig.path("NetworkMode").asText("");
                if (!nm.isEmpty() && !"default".equals(nm)) {
                    network = nm;
                }
            }
            // `wslc create --network` resolves a network by name, not by id, but Testcontainers passes
            // the network id; translate id -> name via a Docker-API network inspect over the bridge.
            addOpt(cmd, "--network", resolveNetworkName(network));

            // Entrypoint override: --entrypoint takes the executable; the rest become leading args.
            List<String> tailArgs = new ArrayList<>();
            JsonNode entrypoint = body.path("Entrypoint");
            if (entrypoint.isArray() && entrypoint.size() > 0) {
                add(cmd, "--entrypoint", entrypoint.get(0).asText());
                for (int i = 1; i < entrypoint.size(); i++) {
                    tailArgs.add(entrypoint.get(i).asText());
                }
            }

            cmd.add(body.path("Image").asText());
            for (JsonNode arg : body.path("Cmd")) {
                tailArgs.add(arg.asText());
            }
            cmd.addAll(tailArgs);

            // ExtraHosts (--add-host) has no wslc equivalent; surface it rather than silently dropping.
            if (hostConfig.path("ExtraHosts").isArray() && hostConfig.path("ExtraHosts").size() > 0) {
                LOGGER.warning("wslc has no --add-host; ExtraHosts ignored: " + hostConfig.path("ExtraHosts"));
            }

            Exec r = run(cmd);
            if (r.exit != 0) {
                LOGGER.warning("wslc create failed (exit " + r.exit + "): " + r.err.trim());
                return json(500, "{\"message\":\"wslc create failed: " + escape(r.err) + "\"}");
            }
            ObjectNode resp = mapper.createObjectNode();
            resp.put("Id", r.out.trim());
            resp.set("Warnings", mapper.createArrayNode());
            return json(201, mapper.writeValueAsString(resp));
        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.WARNING, "wslc create translation failed", ex);
            return json(500, "{\"message\":\"" + escape(String.valueOf(ex.getMessage())) + "\"}");
        }
    }

    // Resolve a docker network reference (id or name) to its name, which is what `wslc create
    // --network` expects. Returns the input unchanged if it is null or cannot be inspected.
    private String resolveNetworkName(String ref) {
        if (ref == null || ref.isEmpty()) {
            return ref;
        }
        try {
            Request req = Request.builder()
                .method(Request.Method.GET)
                .path("/networks/" + ref)
                .headers(java.util.Collections.emptyMap())
                .build();
            try (Response resp = delegate.execute(req)) {
                if (resp.getStatusCode() / 100 == 2) {
                    String name = mapper.readTree(resp.getBody()).path("Name").asText("");
                    if (!name.isEmpty()) {
                        return name;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.fine("network name resolution failed for '" + ref + "', using it as-is: " + e.getMessage());
        }
        return ref;
    }

    private static String text(JsonNode node, String field) {
        String v = node.path(field).asText("");
        return v.isEmpty() ? null : v;
    }

    private static void addOpt(List<String> cmd, String flag, String value) {
        if (value != null && !value.isEmpty()) {
            cmd.add(flag);
            cmd.add(value);
        }
    }

    private static void add(List<String> cmd, String flag, String value) {
        cmd.add(flag);
        cmd.add(value);
    }

    // ---- POST /networks/create -> wslc network create ----
    // wslc create --network only sees networks registered through the wslc control plane; a network
    // created via the Docker API (as Testcontainers does) is invisible to it even on the same daemon.
    // So route network creation through wslc too.
    private Response handleNetworkCreate(Request request) {
        try {
            JsonNode body = mapper.readTree(readBody(request));
            List<String> cmd = new ArrayList<>();
            cmd.add(wslc);
            cmd.add("network");
            cmd.add("create");
            String driver = body.path("Driver").asText("");
            if (!driver.isEmpty() && !"default".equals(driver)) {
                add(cmd, "--driver", driver);
            }
            if (body.path("Internal").asBoolean(false)) {
                cmd.add("--internal");
            }
            for (Iterator<Map.Entry<String, JsonNode>> it = body.path("Options").fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> e = it.next();
                add(cmd, "-o", e.getKey() + "=" + e.getValue().asText());
            }
            for (Iterator<Map.Entry<String, JsonNode>> it = body.path("Labels").fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> e = it.next();
                add(cmd, "-l", e.getKey() + "=" + e.getValue().asText());
            }
            JsonNode ipam = body.path("IPAM").path("Config");
            if (ipam.isArray() && ipam.size() > 0) {
                addOpt(cmd, "--subnet", text(ipam.get(0), "Subnet"));
                addOpt(cmd, "--gateway", text(ipam.get(0), "Gateway"));
            }
            cmd.add(body.path("Name").asText());

            Exec r = run(cmd);
            if (r.exit != 0) {
                LOGGER.warning("wslc network create failed (exit " + r.exit + "): " + r.err.trim());
                return json(500, "{\"message\":\"wslc network create failed: " + escape(r.err) + "\"}");
            }
            ObjectNode resp = mapper.createObjectNode();
            resp.put("Id", r.out.trim());
            resp.put("Warning", "");
            return json(201, mapper.writeValueAsString(resp));
        } catch (Exception ex) {
            LOGGER.log(java.util.logging.Level.WARNING, "wslc network create translation failed", ex);
            return json(500, "{\"message\":\"" + escape(String.valueOf(ex.getMessage())) + "\"}");
        }
    }

    // ---- POST /containers/{id}/start -> wslc start (this is what wires the relay/bind) ----
    private Response handleStart(String id) {
        List<String> cmd = new ArrayList<>();
        cmd.add(wslc);
        cmd.add("start");
        cmd.add(id);
        Exec r = run(cmd);
        if (r.exit != 0) {
            LOGGER.warning("wslc start failed for " + id + " (exit " + r.exit + "): " + r.err.trim());
            return json(500, "{\"message\":\"wslc start failed: " + escape(r.err) + "\"}");
        }
        return json(204, "");
    }

    // ---- GET /containers/{id}/json -> inject the wslc relay port mapping ----
    // wslc publishes ports through its control plane (visible via `wslc list`), but the Docker-API
    // inspect leaves NetworkSettings.Ports empty. Testcontainers reads the mapped host port from
    // there, so fill it in from `wslc list --format json` when the daemon left it blank.
    private Response handleInspect(Request request) {
        Response real = delegate.execute(request);
        int status = real.getStatusCode();
        Map<String, List<String>> headers = real.getHeaders();
        byte[] body;
        try {
            body = readAll(real.getBody());
        } catch (Exception e) {
            return real;
        } finally {
            real.close();
        }
        if (status / 100 != 2) {
            return rawResponse(status, headers, body);
        }
        try {
            JsonNode root = mapper.readTree(body);
            if (root instanceof ObjectNode) {
                ObjectNode obj = (ObjectNode) root;
                // Always override with the wslc ground truth: for a random published port, `wslc create`
                // records one host port in dockerd's inspect but `wslc start`'s relay binds a different
                // one, so the daemon's value is wrong (not just missing). `wslc list` has the real port.
                ObjectNode injected = wslcPorts(obj.path("Id").asText(""));
                if (injected != null && injected.size() > 0) {
                    JsonNode ns = obj.path("NetworkSettings");
                    ObjectNode nsObj = ns.isObject() ? (ObjectNode) ns : obj.putObject("NetworkSettings");
                    nsObj.set("Ports", injected);
                    return rawResponse(status, headers, mapper.writeValueAsBytes(obj));
                }
            }
        } catch (Exception e) {
            LOGGER.fine("inspect port override failed, returning the daemon's original body: " + e.getMessage());
        }
        return rawResponse(status, headers, body);
    }

    // Build a Docker-API NetworkSettings.Ports node for the given container id from `wslc list`.
    private ObjectNode wslcPorts(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        try {
            Exec r = run(java.util.Arrays.asList(wslc, "list", "--format", "json", "--no-trunc", "--all"));
            if (r.exit != 0) {
                LOGGER.fine("wslc list failed (exit " + r.exit + "), leaving inspect ports unchanged: " + r.err.trim());
                return null;
            }
            for (JsonNode row : mapper.readTree(r.out)) {
                String rid = row.path("Id").asText("");
                if (rid.isEmpty() || !(rid.startsWith(id) || id.startsWith(rid))) {
                    continue;
                }
                JsonNode portList = row.path("Ports");
                if (!portList.isArray() || portList.size() == 0) {
                    return null;
                }
                ObjectNode ports = mapper.createObjectNode();
                for (JsonNode p : portList) {
                    String proto = p.path("Protocol").asInt(6) == 17 ? "udp" : "tcp";
                    String key = p.path("ContainerPort").asInt() + "/" + proto;
                    ObjectNode binding = mapper.createObjectNode();
                    binding.put("HostIp", p.path("BindingAddress").asText("127.0.0.1"));
                    binding.put("HostPort", String.valueOf(p.path("HostPort").asInt()));
                    ArrayNode arr = ports.has(key) ? (ArrayNode) ports.get(key) : ports.putArray(key);
                    arr.add(binding);
                }
                return ports;
            }
        } catch (Exception e) {
            LOGGER.fine("could not derive wslc port mapping for " + id + ": " + e.getMessage());
        }
        return null;
    }

    private static byte[] readAll(InputStream in) throws IOException {
        if (in == null) {
            return new byte[0];
        }
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = in.read(buf)) != -1) {
            bos.write(buf, 0, n);
        }
        return bos.toByteArray();
    }

    private static Response rawResponse(int status, Map<String, List<String>> headers, byte[] body) {
        return new Response() {
            @Override public int getStatusCode() {
                return status;
            }
            @Override public Map<String, List<String>> getHeaders() {
                return headers;
            }
            @Override public InputStream getBody() {
                return new ByteArrayInputStream(body);
            }
            @Override public void close() {
            }
        };
    }

    // ---------- helpers ----------
    private static String queryParam(String path, String key) {
        int q = path.indexOf('?');
        if (q < 0) {
            return null;
        }
        for (String pair : path.substring(q + 1).split("&")) {
            int eq = pair.indexOf('=');
            if (eq > 0 && pair.substring(0, eq).equals(key)) {
                return urlDecode(pair.substring(eq + 1));
            }
        }
        return null;
    }

    private static String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (Exception e) {
            return s;
        }
    }

    private static byte[] readBody(Request request) throws IOException {
        if (request.bodyBytes() != null) {
            return request.bodyBytes();
        }
        InputStream in = request.body();
        if (in == null) {
            return new byte[0];
        }
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = in.read(buf)) != -1) {
            bos.write(buf, 0, n);
        }
        return bos.toByteArray();
    }

    private static final class Exec {
        final int exit;
        final String out;
        final String err;
        Exec(int exit, String out, String err) {
            this.exit = exit;
            this.out = out;
            this.err = err;
        }
    }

    private static Exec run(List<String> command) {
        Process p = null;
        try {
            p = new ProcessBuilder(command).start();
            // Drain stdout and stderr concurrently: reading one fully before the other would risk a
            // deadlock if the process fills the still-unread pipe's buffer while we block on the other.
            final Process proc = p;
            final StringBuilder errBuf = new StringBuilder();
            Thread errDrain = new Thread(() -> {
                try {
                    errBuf.append(drain(proc.getErrorStream()));
                } catch (IOException ignored) {
                    // process gone; nothing useful to add
                }
            }, "wslc-cmd-stderr");
            errDrain.setDaemon(true);
            errDrain.start();
            String out = drain(p.getInputStream());
            if (!p.waitFor(120, TimeUnit.SECONDS)) {
                p.destroyForcibly();
                errDrain.join(1000);
                return new Exec(-1, out, "timed out after 120s");
            }
            errDrain.join(1000);
            return new Exec(p.exitValue(), out, errBuf.toString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            if (p != null) {
                p.destroyForcibly();
            }
            return new Exec(-1, "", "interrupted");
        } catch (Exception e) {
            if (p != null) {
                p.destroyForcibly();
            }
            return new Exec(-1, "", String.valueOf(e.getMessage()));
        }
    }

    private static String drain(InputStream in) throws IOException {
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = in.read(buf)) != -1) {
            bos.write(buf, 0, n);
        }
        return new String(bos.toByteArray(), StandardCharsets.UTF_8);
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ").trim();
    }

    private static Response json(int status, String body) {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        return new Response() {
            @Override public int getStatusCode() {
                return status;
            }
            @Override public Map<String, List<String>> getHeaders() {
                return Collections.singletonMap("Content-Type", Collections.singletonList("application/json"));
            }
            @Override public InputStream getBody() {
                return new ByteArrayInputStream(bytes);
            }
            @Override public void close() {
            }
        };
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}
