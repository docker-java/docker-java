package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
    public class HostConfig {

        @JsonProperty("Binds")
        private String[] binds;

        @JsonProperty("LxcConf")
        private LxcConf[] lxcConf;

        @JsonProperty("PortBindings")
        private Ports portBindings;

        @JsonProperty("PublishAllPorts")
        private boolean publishAllPorts;

        @JsonProperty("Privileged")
        private boolean privileged;

        @JsonProperty("Dns")
        private String[] dns;

        @JsonProperty("VolumesFrom")
        private String[] volumesFrom;

        @JsonProperty("ContainerIDFile")
        private String containerIDFile;

        @JsonProperty("DnsSearch")
        private String dnsSearch;

        // TODO: use Links class here?
        @JsonProperty("Links")
        private String[] links;

        @JsonProperty("NetworkMode")
        private String networkMode;

        public String[] getBinds() {
            return binds;
		}

        public LxcConf[] getLxcConf() {
			return lxcConf;
		}

		public Ports getPortBindings() {
			return portBindings;
		}

		public boolean isPublishAllPorts() {
			return publishAllPorts;
		}

		public boolean isPrivileged() {
			return privileged;
		}

		public String[] getDns() {
			return dns;
		}

		public String[] getVolumesFrom() {
			return volumesFrom;
		}

		public String getContainerIDFile() {
			return containerIDFile;
		}

		public String getDnsSearch() {
			return dnsSearch;
		}

		public String[] getLinks() {
			return links;
		}

		public String getNetworkMode() {
			return networkMode;
		}

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

    }