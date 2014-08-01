package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.client.model.*;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


/**
 *
 * Creates a new container.
 *
 */
public class CreateContainerCmd extends AbstrDockerCmd<CreateContainerCmd, CreateContainerResponse>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateContainerCmd.class);

	private CreateContainerConfig containerCreateConfig;
	private String name;

	public CreateContainerCmd(String image) {
		this(new CreateContainerConfig());
		Preconditions.checkNotNull(image, "image was not specified");
		this.containerCreateConfig.withImage(image);
	}

	private CreateContainerCmd(CreateContainerConfig config) {
		Preconditions.checkNotNull(config, "config was not specified");
		this.containerCreateConfig = config;
	}

    public String getName() {
        return name;
    }

    public CreateContainerCmd withImage(String image) {
		Preconditions.checkNotNull(image, "image was not specified");
		this.containerCreateConfig.withImage(image);
		return this;
	}

	public CreateContainerCmd withCmd(String... cmd) {
		Preconditions.checkNotNull(cmd, "cmd was not specified");
		this.containerCreateConfig.withCmd(cmd);
		return this;
	}

	public CreateContainerCmd withVolumes(Volume... volumes) {
		Preconditions.checkNotNull(volumes, "volumes was not specified");
		this.containerCreateConfig.withVolumes(volumes);
		return this;
	}

	public CreateContainerCmd withVolumesFrom(String... volumesFrom) {
		Preconditions.checkNotNull(volumesFrom, "volumes was not specified");
		this.containerCreateConfig.withVolumesFrom(volumesFrom);
		return this;
	}

    public CreateContainerCmd withEnv(String... env) {
            Preconditions.checkNotNull(env, "env was not specified");
            this.containerCreateConfig.withEnv(env);
            return this;
    }

    public CreateContainerCmd withHostName(String hostName) {
            Preconditions.checkNotNull(hostName, "hostName was not specified");
            this.containerCreateConfig.withHostName(hostName);
            return this;
    }

	public CreateContainerCmd withName(String name) {
		Preconditions.checkNotNull(name, "name was not specified");
		this.name = name;
		return this;
	}

	public CreateContainerCmd withExposedPorts(ExposedPort... exposedPorts) {
		Preconditions.checkNotNull(exposedPorts, "exposedPorts was not specified");

		this.containerCreateConfig.withExposedPorts(exposedPorts);
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("create container ")
            .append(name != null ? "name=" + name + " " : "")
            .append(containerCreateConfig)
            .toString();
    }

	protected CreateContainerResponse impl() {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		if (name != null) {
			params.add("name", name);
		}
		WebResource webResource = baseResource.path("/containers/create").queryParams(params);

		try {
			LOGGER.trace("POST: {} ", webResource);
			return webResource.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
					.post(CreateContainerResponse.class, containerCreateConfig);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("%s is an unrecognized image. Please pull the image first.", containerCreateConfig.getImage()));
			} else if (exception.getResponse().getStatus() == 406) {
				throw new DockerException("impossible to attach (container not running)");
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}

    /**
     *
     * @author Konstantin Pelykh (kpelykh@gmail.com)
     *
     * 		"Hostname":"",
             "User":"",
             "Memory":0,
             "MemorySwap":0,
             "AttachStdin":false,
             "AttachStdout":true,
             "AttachStderr":true,
             "PortSpecs":null,
             "Tty":false,
             "OpenStdin":false,
             "StdinOnce":false,
             "Env":null,
             "Cmd":[
                     "date"
             ],
             "Dns":null,
             "Image":"base",
             "Volumes":{
                     "/tmp": {}
             },
             "VolumesFrom":"",
             "WorkingDir":"",
             "DisableNetwork": false,
             "ExposedPorts":{
                     "22/tcp": {}
             }
     *
     *
     */
    private static class CreateContainerConfig {

        @JsonProperty("Hostname")     private String    hostName = "";
        @JsonProperty("User")         private String    user = "";
        @JsonProperty("Memory")       private long      memoryLimit = 0;
        @JsonProperty("MemorySwap")   private long      memorySwap = 0;
        @JsonProperty("AttachStdin")  private boolean   attachStdin = false;
        @JsonProperty("AttachStdout") private boolean   attachStdout = false;
        @JsonProperty("AttachStderr") private boolean   attachStderr = false;
        @JsonProperty("PortSpecs")    private String[]  portSpecs;
        @JsonProperty("Tty")          private boolean   tty = false;
        @JsonProperty("OpenStdin")    private boolean   stdinOpen = false;
        @JsonProperty("StdinOnce")    private boolean   stdInOnce = false;
        @JsonProperty("Env")          private String[]  env;
        @JsonProperty("Cmd")          private String[]  cmd;
        @JsonProperty("Dns")          private String[]  dns;
        @JsonProperty("Image")        private String    image;
        @JsonProperty("Volumes")      private Volumes volumes = new Volumes();
        @JsonProperty("VolumesFrom")  private String[]    volumesFrom = new String[]{};
        @JsonProperty("WorkingDir")   private String workingDir = "";
        @JsonProperty("DisableNetwork") private boolean disableNetwork = false;
        @JsonProperty("ExposedPorts")   private ExposedPorts exposedPorts = new ExposedPorts();

        public CreateContainerConfig withExposedPorts(ExposedPort[] exposedPorts) {
            this.exposedPorts = new ExposedPorts(exposedPorts);
            return this;
        }

        @JsonIgnore
        public ExposedPort[] getExposedPorts() {
            return exposedPorts.getExposedPorts();
        }


        public boolean isDisableNetwork() {
            return disableNetwork;
        }

        public String getWorkingDir() { return workingDir; }

        public CreateContainerConfig withWorkingDir(String workingDir) {
            this.workingDir = workingDir;
            return this;
        }


        public String getHostName() {
            return hostName;
        }

        public CreateContainerConfig withDisableNetwork(boolean disableNetwork) {
            this.disableNetwork = disableNetwork;
            return this;
        }

        public CreateContainerConfig withHostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public String[] getPortSpecs() {
            return portSpecs;
        }

        public CreateContainerConfig withPortSpecs(String[] portSpecs) {
            this.portSpecs = portSpecs;
            return this;
        }

        public String getUser() {
            return user;
        }

        public CreateContainerConfig withUser(String user) {
            this.user = user;
            return this;
        }

        public boolean isTty() {
            return tty;
        }

        public CreateContainerConfig withTty(boolean tty) {
            this.tty = tty;
            return this;
        }

        public boolean isStdinOpen() {
            return stdinOpen;
        }

        public CreateContainerConfig withStdinOpen(boolean stdinOpen) {
            this.stdinOpen = stdinOpen;
            return this;
        }

        public boolean isStdInOnce() {
            return stdInOnce;
        }

        public CreateContainerConfig withStdInOnce(boolean stdInOnce) {
            this.stdInOnce = stdInOnce;
            return this;
        }

        public long getMemoryLimit() {
            return memoryLimit;
        }

        public CreateContainerConfig withMemoryLimit(long memoryLimit) {
            this.memoryLimit = memoryLimit;
            return this;
        }

        public long getMemorySwap() {
            return memorySwap;
        }

        public CreateContainerConfig withMemorySwap(long memorySwap) {
            this.memorySwap = memorySwap;
            return this;
        }


        public boolean isAttachStdin() {
            return attachStdin;
        }

        public CreateContainerConfig withAttachStdin(boolean attachStdin) {
            this.attachStdin = attachStdin;
            return this;
        }

        public boolean isAttachStdout() {
            return attachStdout;
        }

        public CreateContainerConfig withAttachStdout(boolean attachStdout) {
            this.attachStdout = attachStdout;
            return this;
        }

        public boolean isAttachStderr() {
            return attachStderr;
        }

        public CreateContainerConfig withAttachStderr(boolean attachStderr) {
            this.attachStderr = attachStderr;
            return this;
        }

        public String[] getEnv() {
            return env;
        }

        public CreateContainerConfig withEnv(String[] env) {
            this.env = env;
            return this;
        }

        public String[] getCmd() {
            return cmd;
        }

        public CreateContainerConfig withCmd(String[] cmd) {
            this.cmd = cmd;
            return this;
        }

        public String[] getDns() {
            return dns;
        }

        public CreateContainerConfig withDns(String[] dns) {
            this.dns = dns;
            return this;
        }

        public String getImage() {
            return image;
        }

        public CreateContainerConfig withImage(String image) {
            this.image = image;
            return this;
        }

        @JsonIgnore
        public Volume[] getVolumes() {
            return volumes.getVolumes();
        }

        public CreateContainerConfig withVolumes(Volume[] volumes) {
            this.volumes = new Volumes(volumes);
            return this;
        }

        public String[] getVolumesFrom() {
            return volumesFrom;
        }

        public CreateContainerConfig withVolumesFrom(String[] volumesFrom) {
            this.volumesFrom = volumesFrom;
            return this;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }


    }
}
