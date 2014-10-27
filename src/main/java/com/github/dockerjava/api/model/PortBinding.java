package com.github.dockerjava.api.model;

import com.github.dockerjava.api.command.InspectContainerResponse.HostConfig;
import com.github.dockerjava.api.command.InspectContainerResponse.NetworkSettings;
import com.github.dockerjava.api.model.Ports.Binding;

/**
 * In a {@link PortBinding}, a network socket on the Docker host, expressed 
 * as a {@link Binding}, is bound to an {@link ExposedPort} of a container.
 * A {@link PortBinding} corresponds to the <code>--publish</code>
 * (<code>-p</code>) option of the <code>docker run</code> (and similar)
 * CLI command for adding port bindings to a container.
 * <p>
 * <i>Note: This is an abstraction used for creating new port bindings.
 * It is not to be confused with the abstraction used for querying existing
 * port bindings from a container configuration in 
 * {@link NetworkSettings#getPorts()} and {@link HostConfig#getPortBindings()}.
 * In that context, a <code>Map&lt;ExposedPort, Binding[]&gt;</code> is used.</i>
 */
public class PortBinding {
	private final Binding binding;
	private final ExposedPort exposedPort;

	public PortBinding(Binding binding, ExposedPort exposedPort) {
		this.binding = binding;
		this.exposedPort = exposedPort;
	}

	public Binding getBinding() {
		return binding;
	}

	public ExposedPort getExposedPort() {
		return exposedPort;
	}
}
