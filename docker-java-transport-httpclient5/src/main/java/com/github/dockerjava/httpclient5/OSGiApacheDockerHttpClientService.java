package com.github.dockerjava.httpclient5;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClientType;

@DockerHttpClientType(value = OSGiApacheDockerHttpClientService.APACHE_HTTPCLIENT_5, transports = { DockerHttpClientType.TRANSPORT_STDIN_ATTACHMENT,
        DockerHttpClientType.TRANSPORT_WINDOWS_NPIPE, DockerHttpClientType.TRANSPORT_UNIX_SOCKETS })
@Component(service = DockerHttpClient.class, immediate = true, name = OSGiApacheDockerHttpClientService.PID)
public class OSGiApacheDockerHttpClientService extends ApacheDockerHttpClientImpl {

    static final String APACHE_HTTPCLIENT_5 = "apache.httpclient.5";
    public static final String PID = "com.github.dockerjava.httpclient5.basic";

    @ObjectClassDefinition(pid = PID)
    @interface Config {
        String dockerHost() default "localhost";

        int maxConnections() default Integer.MAX_VALUE;;

        long connectionTimeoutNanos() default -1;

        long responseTimeoutNanos() default -1;
    }

    @Activate
    protected OSGiApacheDockerHttpClientService(Config config) {
        super(toDockerHost(config.dockerHost()), null, config.maxConnections(),
                toDurationNanos(config.connectionTimeoutNanos()), toDurationNanos(config.responseTimeoutNanos()));
    }

    private static Duration toDurationNanos(long value) {
        if (value < 0) {
            return null;
        }
        return Duration.ofNanos(value);
    }

    private static URI toDockerHost(String config) {
        return URI.create(config);
    }

    @Deactivate
    public void deactivate() throws IOException {
        close();
    }

}
