package com.github.dockerjava.core.dockerfile;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DockerfileTest extends TestCase {

  private static final Logger log = LoggerFactory.getLogger(DockerfileTest.class);

  @Test
  public void testAllItems() throws IOException {
    File baseDir = new File(Thread.currentThread().getContextClassLoader()
                                .getResource("netcat").getFile());

    File root = baseDir.getParentFile();

    Map<String, Dockerfile> dockerfiles = new HashMap<String, Dockerfile>();
    Map<String, Dockerfile.ScannedResult> results = new HashMap<String, Dockerfile.ScannedResult>();

    for (File child : root.listFiles()) {
      if (new File(child, "Dockerfile").exists()) {
        Dockerfile dockerfile = new Dockerfile(new File(child, "Dockerfile"));
        dockerfiles.put(child.getName(), dockerfile);
      }
    }

    for (String name : dockerfiles.keySet()) {
      log.info("Scanning {}", name);
      try {
        results.put(name, dockerfiles.get(name).parse());
      } catch (Exception ex) {
        log.error("Error in {}", name, ex);
      }

    }

    for (String name : results.keySet()) {
      log.info("Name: {} = {}", name, results.get(name));
    }
  }

}