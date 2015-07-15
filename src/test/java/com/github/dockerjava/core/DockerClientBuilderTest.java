package com.github.dockerjava.core;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.annotations.Test;

import com.github.dockerjava.api.command.DockerCmdExecFactory;

public class DockerClientBuilderTest {
    // Amount of instances created in test
    private static final int AMOUNT = 100;

    @Test
    public void testConcurrentClientBuilding() throws Exception {
        // we use it to check instance uniqueness
        final Set<DockerCmdExecFactory> instances = Collections.synchronizedSet(new HashSet<DockerCmdExecFactory>());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DockerCmdExecFactory factory = DockerClientBuilder.getDefaultDockerCmdExecFactory();
                // factory created
                assertNotNull(factory);
                // and is unique
                assertFalse(instances.contains(factory));
                instances.add(factory);
            }
        };

        parallel(AMOUNT, runnable);
        // set contains all required unique instances
        assertEquals(instances.size(), AMOUNT);
    }

    public static void parallel(int threads, final Runnable task) throws Exception {
        final ExceptionListener exceptionListener = new ExceptionListener();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Throwable e) {
                    exceptionListener.onException(e);
                }
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
