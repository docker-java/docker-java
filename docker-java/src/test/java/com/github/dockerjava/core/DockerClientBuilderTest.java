package com.github.dockerjava.core;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import java.time.Duration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class DockerClientBuilderTest {
    // Amount of instances created in test
    private static final int AMOUNT = 100;

    @Test
    public void testConcurrentClientBuilding() throws Exception {
        // we use it to check instance uniqueness
        final Set<DockerCmdExecFactory> instances = Collections.synchronizedSet(new HashSet<>());

        Runnable runnable = () -> {
            DockerCmdExecFactory factory = DockerClientBuilder.getDefaultDockerCmdExecFactory();
            // factory created
            assertNotNull(factory);
            // and is unique
            assertFalse(instances.contains(factory));
            instances.add(factory);
        };

        parallel(AMOUNT, runnable);
        // set contains all required unique instances
        assertEquals(AMOUNT, instances.size());
    }

    public static void parallel(int threads, final Runnable task) throws Exception {
        final ExceptionListener exceptionListener = new ExceptionListener();
        Runnable runnable = () -> {
            try {
                task.run();
            } catch (Throwable e) {
                exceptionListener.onException(e);
            }
        };

        List<Thread> threadList = new ArrayList<>(threads);
        for (int i = 0; i < threads; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
            threadList.add(thread);
        }
        for (Thread thread : threadList) {
            thread.join();
        }
        Throwable exception = exceptionListener.getException();
        if (exception != null) {
            throw new RuntimeException(exception);
        }
    }

    private static class ExceptionListener {
        private Throwable exception;

        private synchronized void onException(Throwable e) {
            exception = e;
        }

        private synchronized Throwable getException() {
            return exception;
        }
    }
}
