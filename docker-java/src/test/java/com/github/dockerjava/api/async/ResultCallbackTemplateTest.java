package com.github.dockerjava.api.async;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResultCallbackTemplateTest {

    static Set<URL> CLASSPATH = Stream.of(System.getProperty("java.class.path").split(":"))
            .map(it -> {
                try {
                    return new File(it).toURI().toURL();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            })
            .collect(Collectors.toSet());

    @Test
    public void shouldNotFailIfSlf4jIsNotOnClasspath() throws Exception {
        try (
                URLClassLoader classLoader = new URLClassLoader(
                        CLASSPATH.stream().filter(it -> !it.getPath().contains("slf4j")).toArray(URL[]::new),
                        null
                )
        ) {
            Class<?> clazz = classLoader.loadClass(DummyResultCallbackTemplate.class.getName());
            Object template = clazz.newInstance();
            AtomicBoolean classNotFound = new AtomicBoolean();
            clazz.getMethod("onNext", AtomicBoolean.class).invoke(template, classNotFound);
            Assert.assertTrue("Slf4J is not on classpath", classNotFound.get());
            clazz.getMethod("onError", Throwable.class).invoke(template, new Exception("Boom"));
        }
    }

    public static class DummyResultCallbackTemplate extends ResultCallbackTemplate<DummyResultCallbackTemplate, AtomicBoolean> {

        @Override
        public void onNext(AtomicBoolean atomicBoolean) {
            try {
                atomicBoolean.set(Class.forName("org.slf4j.LoggerFactory") == null);
            } catch (ClassNotFoundException ignored) {
                atomicBoolean.set(true);
            }
        }
    }
}