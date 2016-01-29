package com.github.dockerjava.api.model;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

public class ContainerTest extends Assert {

    @Test
    public void parseSwarmNames() throws Exception {
        Container cnt = create("/swarmhost/auth/etcd", "/swarmhost/etcd", "/swarmhost/foo/etcd");
        assertEquals(cnt.getNameInSwarm(), "etcd");
        assertEquals(cnt.getSwarmHost(), "swarmhost");
    }

    @Test
    public void parseSwarmNames2() throws Exception {
        Container cnt = create("/swarmhost/etcd");
        assertEquals(cnt.getNameInSwarm(), "etcd");
        assertEquals(cnt.getSwarmHost(), "swarmhost");
    }

    private Container create(String ...names) throws Exception {
        Container cnt = new Container();

        Field fNames = Container.class.getDeclaredField("names");
        fNames.setAccessible(true);
        fNames.set(cnt, names);

        return cnt;
    }
}
