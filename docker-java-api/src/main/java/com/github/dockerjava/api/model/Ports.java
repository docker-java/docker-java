package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A container for port bindings, made available as a {@link Map} via its {@link #getBindings()} method.
 * <p>
 * <i>Note: This is an abstraction used for querying existing port bindings from a container configuration. It is not to be confused with
 * the {@link PortBinding} abstraction used for adding new port bindings to a container.</i>
 *
 * @see HostConfig#getPortBindings()
 * @see NetworkSettings#getPorts()
 */
@SuppressWarnings(value = "checkstyle:equalshashcode")
public class Ports implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<ExposedPort, Binding[]> ports = new HashMap<>();

    /**
     * Creates a {@link Ports} object with no {@link PortBinding}s. Use {@link #bind(ExposedPort, Binding)} or {@link #add(PortBinding...)}
     * to add {@link PortBinding}s.
     */
    public Ports() {
    }

    /**
     * Creates a {@link Ports} object with an initial {@link PortBinding} for the specified {@link ExposedPort} and {@link Binding}. Use
     * {@link #bind(ExposedPort, Binding)} or {@link #add(PortBinding...)} to add more {@link PortBinding}s.
     */
    public Ports(ExposedPort exposedPort, Binding host) {
        bind(exposedPort, host);
    }

    public Ports(PortBinding... portBindings) {
        add(portBindings);
    }

    /**
     * Adds a new {@link PortBinding} for the specified {@link ExposedPort} and {@link Binding} to the current bindings.
     */
    public void bind(ExposedPort exposedPort, Binding binding) {
        if (ports.containsKey(exposedPort)) {
            Binding[] bindings = ports.get(exposedPort);
            Binding[] newBindings = new Binding[bindings.length + 1];
            System.arraycopy(bindings, 0, newBindings, 0, bindings.length);
            newBindings[newBindings.length - 1] = binding;
            ports.put(exposedPort, newBindings);
        } else {
            if (binding == null) {
                ports.put(exposedPort, null);
            } else {
                ports.put(exposedPort, new Binding[] {binding});
            }
        }
    }

    /**
     * Adds the specified {@link PortBinding}(s) to the list of {@link PortBinding}s.
     */
    public void add(PortBinding... portBindings) {
        for (PortBinding binding : portBindings) {
            bind(binding.getExposedPort(), binding.getBinding());
        }
    }

    @Override
    public String toString() {
        return ports.toString();
    }

    /**
     * Returns the port bindings in the format used by the Docker remote API, i.e. the {@link Binding}s grouped by {@link ExposedPort}.
     *
     * @return the port bindings as a {@link Map} that contains one or more {@link Binding}s per {@link ExposedPort}.
     */
    public Map<ExposedPort, Binding[]> getBindings() {
        return ports;
    }

    // public PortBinding[] getBindingsAsArray() {
    // List<PortBinding> bindings = new ArrayList<>();
    // for(Map.Entry<ExposedPort, Ports.Binding[]> entry: ports.entrySet()) {
    // for(Ports.Binding binding : entry.getValue()) {
    // bindings.add(new PortBinding(binding, entry.getKey()));
    // }
    // }
    // return bindings.toArray(new PortBinding[bindings.size()]);
    // }

    /**
     * A {@link Binding} represents a socket on the Docker host that is used in a {@link PortBinding}. It is characterized by an
     * {@link #getHostIp() IP address} and a {@link #getHostPortSpec() port spec}. Both properties may be <code>null</code> in order to
     * let Docker assign them dynamically/using defaults.
     *
     * @see Ports#bind(ExposedPort, Binding)
     * @see ExposedPort
     */
    @EqualsAndHashCode
    public static class Binding implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * Creates a {@link Binding} for the given {@link #getHostPortSpec() port spec}, leaving the {@link #getHostIp() IP address}
         * undefined.
         *
         * @see Ports#bind(ExposedPort, Binding)
         * @see ExposedPort
         */
        public static Binding bindPortSpec(String portSpec) {
            return new Binding(null, portSpec);
        }

        /**
         * Creates a {@link Binding} for the given {@link #getHostIp() IP address}, leaving the {@link #getHostPortSpec() port spec}
         * undefined.
         */
        public static Binding bindIp(String hostIp) {
            return new Binding(hostIp, null);
        }

        /**
         * Creates a {@link Binding} for the given {@link #getHostIp() IP address} and port number.
         */
        public static Binding bindIpAndPort(String hostIp, int port) {
            return new Binding(hostIp, "" + port);
        }

        /**
         * Creates a {@link Binding} for the given {@link #getHostIp() IP address} and port range.
         */
        public static Binding bindIpAndPortRange(String hostIp, int lowPort, int highPort) {
            return new Binding(hostIp, lowPort + "-" + highPort);
        }

        /**
         * Creates a {@link Binding} for the given port range, leaving the {@link #getHostIp() IP address}
         * undefined.
         */
        public static Binding bindPortRange(int lowPort, int highPort) {
            return bindIpAndPortRange(null, lowPort, highPort);
        }

        /**
         * Creates a {@link Binding} for the given port leaving the {@link #getHostIp() IP address}
         * undefined.
         */
        public static Binding bindPort(int port) {
            return bindIpAndPort(null, port);
        }

        /**
         * Creates an empty {@link Binding}.
         */
        public static Binding empty() {
            return new Binding(null, null);
        }

        private final String hostIp;

        private final String hostPortSpec;

        /**
         * Creates a {@link Binding} for the given {@link #getHostIp() host IP address} and {@link #getHostPortSpec() host port spec}.
         *
         * @see Ports#bind(ExposedPort, Binding)
         * @see ExposedPort
         */
        public Binding(String hostIp, String hostPortSpec) {
            this.hostIp = hostIp == null || hostIp.length() == 0 ? null : hostIp;
            this.hostPortSpec = hostPortSpec;
        }

        /**
         * @return the IP address on the Docker host. May be <code>null</code>, in which case Docker will bind the port to all interfaces (
         *         <code>0.0.0.0</code>).
         */
        public String getHostIp() {
            return hostIp;
        }

        /**
         * @return the port spec for the binding on the Docker host. May reference a single port ("1234"), a port range ("1234-2345") or
         *         <code>null</code>, in which case Docker will dynamically assign a port.
         */
        public String getHostPortSpec() {
            return hostPortSpec;
        }

        /**
         * Parses a textual host and port specification (as used by the Docker CLI) to a {@link Binding}.
         * <p>
         * Legal syntax: <code>IP|IP:portSpec|portSpec</code> where <code>portSpec</code> is either a single port or a port range
         *
         * @param serialized
         *            serialized the specification, e.g. <code>127.0.0.1:80</code>
         * @return a {@link Binding} matching the specification
         * @throws IllegalArgumentException
         *             if the specification cannot be parsed
         */
        public static Binding parse(String serialized) throws IllegalArgumentException {
            try {
                if (serialized.isEmpty()) {
                    return Binding.empty();
                }

                String[] parts = serialized.split(":");
                switch (parts.length) {
                    case 2: {
                        return new Binding(parts[0], parts[1]);
                    }
                    case 1: {
                        return parts[0].contains(".") ? Binding.bindIp(parts[0]) : Binding.bindPortSpec(parts[0]);
                    }
                    default: {
                        throw new IllegalArgumentException();
                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Error parsing Binding '" + serialized + "'");
            }
        }

        /**
         * Returns a string representation of this {@link Binding} suitable for inclusion in a JSON message. The format is
         * <code>[IP:]Port</code>, like the argument in {@link #parse(String)}.
         *
         * @return a string representation of this {@link Binding}
         */
        @Override
        public String toString() {
            if (hostIp == null || hostIp.length() == 0) {
                return hostPortSpec;
            } else if (hostPortSpec == null) {
                return hostIp;
            } else {
                return hostIp + ":" + hostPortSpec;
            }
        }
    }

    @JsonCreator
    public static Ports fromPrimitive(Map<String, List<Map<String, String>>> map) {
        Ports out = new Ports();
        for (Entry<String, List<Map<String, String>>> entry : map.entrySet()) {
            ExposedPort exposedPort = ExposedPort.parse(entry.getKey());

            if (entry.getValue() == null) {
                out.bind(exposedPort, null);
            } else {
                for (Map<String, String> binding : entry.getValue()) {
                    out.bind(exposedPort, new Binding(binding.get("HostIp"), binding.get("HostPort")));
                }
            }
        }
        return out;
    }

    @JsonValue
    public Map<String, List<Map<String, String>>> toPrimitive() {
        // Use reduce-like collect to be able to put nulls into the values
        return ports.entrySet().stream().collect(
                HashMap::new,
                (map, entry) -> {
                    List<Map<String, String>> value = entry.getValue() == null ? null : Stream.of(entry.getValue())
                            .map(binding -> {
                                Map<String, String> result = new HashMap<>();
                                result.put("HostIp", binding.getHostIp() == null ? "" : binding.getHostIp());
                                result.put("HostPort", binding.getHostPortSpec() == null ? "" : binding.getHostPortSpec());
                                return result;
                            })
                            .collect(Collectors.toList());
                    map.put(entry.getKey().toString(), value);
                },
                HashMap::putAll
        );
    }
}
