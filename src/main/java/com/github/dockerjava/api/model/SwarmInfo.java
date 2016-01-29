package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.*;

/**
 *
 * @author Michael Freund (fragger@xfragger.de)
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SwarmInfo extends Info {

    @SuppressWarnings("unchecked")
    private static List<String> castLine(Object line) {
        return (List<String>)line;
    }

    @JsonIgnore
    public Map<String, Node> getSwarmNodes() {
        final Map<String, Node> nodes = new HashMap<String, Node>();

        boolean nodesFound = false;
        Node lastNode = null;

        for(Object lineObj : getDriverStatuses()) {
            List<String> lineArr = castLine(lineObj);

            //these strings have control chars (backspace as first char for example), remove all of them
            String key = lineArr.get(0).replaceAll("[\u0000-\u001f]", "");

            if (key.equals("Nodes")) {
                nodesFound = true;
                continue;
            }

            if (nodesFound && !key.substring(0, 3).equals(" └ ")) {
                Node node = new Node(key, lineArr.get(1));
                nodes.put(key, node);
                lastNode = node;
                continue;
            }

            if (lastNode != null) {
                switch (key) {
                    case " └ Status":
                        lastNode.status = lineArr.get(1);
                        break;
                    case " └ Containers":
                        lastNode.containers = Integer.valueOf(lineArr.get(1));
                        break;
                    case " └ Reserved CPUs":
                        String[] splittedCPU = lineArr.get(1).split("/");
                        lastNode.reservedCpus = Integer.valueOf(splittedCPU[0].trim());
                        lastNode.cpus = Integer.valueOf(splittedCPU[1].trim());
                        break;
                    case " └ Reserved Memory":
                        String[] splittedMem = lineArr.get(1).split("/");
                        lastNode.reservedMemory = splittedMem[0].trim();
                        lastNode.memory = splittedMem[1].trim();
                        break;
                    case " └ Labels":
                        for (String label : lineArr.get(1).split(", ")) {
                            String[] split = label.split("=");
                            lastNode.labels.put(split[0], split[1]);
                        }
                        break;
                    default:
                        //ignore unknown nodes for the moment
                }
            }
        }

        return nodes;
    }

    public class Node {
        private final String name;
        private final String ip;
        private final int port;
        private final Map<String, String> labels = new HashMap<>();
        private String status;
        private int containers;
        private int cpus;
        private int reservedCpus;
        private String memory;
        private String reservedMemory;

        public Node(String name, String ipPort) {
            String[] splitted = ipPort.split(":");
            this.name = name;
            this.ip = splitted[0];
            this.port = Integer.valueOf(splitted[1]);
        }

        public String getName() {
            return name;
        }

        public String getIp() {
            return ip;
        }

        public int getPort() {
            return port;
        }

        public String getStatus() {
            return status;
        }

        public int getContainers() {
            return containers;
        }

        public int getCpus() {
            return cpus;
        }

        public int getReservedCpus() {
            return reservedCpus;
        }

        public String getMemory() {
            return memory;
        }

        public String getReservedMemory() {
            return reservedMemory;
        }

        public Map<String, String> getLabels() {
            return labels;
        }
    }
}
