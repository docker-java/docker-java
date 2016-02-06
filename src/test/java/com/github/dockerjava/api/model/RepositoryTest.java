package com.github.dockerjava.api.model;

import junit.framework.TestCase;

public class RepositoryTest extends TestCase {
    public void testRepository() throws Exception {

        Repository repo = new Repository("10.0.0.1/jim");
        Repository repo1 = new Repository("10.0.0.1:1234/jim");
        Repository repo2 = new Repository("busybox");

        assertEquals("jim", repo.getPath());
        assertEquals("jim", repo1.getPath());
        assertEquals("busybox", repo2.getPath());

        assertEquals(1234, repo1.getURL().getPort());
    }
}
