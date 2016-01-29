package com.github.dockerjava.api.model;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static com.github.dockerjava.test.serdes.JSONTestHelper.testRoundTrip;

public class SwarmInfoTest extends Assert {

    @Test
    public void getSwarmNodes() throws Exception {

        SwarmInfo info = testRoundTrip(SwarmInfoJSONSamples.infoResponse_swarm, SwarmInfo.class);

        Map<String, SwarmInfo.Node> nodes = info.getSwarmNodes();
        assertEquals(nodes.size(), 2);
        assertTrue(nodes.containsKey("node1"));
        assertTrue(nodes.containsKey("node2"));

        SwarmInfo.Node n1 = nodes.get("node1");
        assertEquals(n1.getName(), "node1");
        assertEquals(n1.getIp(), "10.30.2.28");
        assertEquals(n1.getPort(), 2375);
        assertEquals(n1.getContainers(), 12);
        assertEquals(n1.getCpus(), 1);
        assertEquals(n1.getReservedCpus(), 0);
        assertEquals(n1.getMemory(), "1.017 GiB");
        assertEquals(n1.getReservedMemory(), "0 B");
        assertEquals(n1.getStatus(), "Healthy");

        Map<String, String> labels = n1.getLabels();
        assertEquals(labels.size(), 4);
        assertEquals(labels.get("executiondriver"), "native-0.2");
        assertEquals(labels.get("kernelversion"), "3.10.0-327.4.4.el7.x86_64");
        assertEquals(labels.get("operatingsystem"), "CentOS Linux 7 (Core)");
        assertEquals(labels.get("storagedriver"), "devicemapper");
    }



}
