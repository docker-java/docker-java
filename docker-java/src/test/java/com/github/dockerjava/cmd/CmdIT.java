package com.github.dockerjava.cmd;

import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.DockerRule;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.junit.category.Integration;
import com.github.dockerjava.transport.DockerHttpClient;
import org.junit.Rule;
import org.junit.experimental.categories.Category;

/**
 * @author Kanstantsin Shautsou
 */
@Category(Integration.class)
public abstract class CmdIT {

    public static DockerHttpClient createDockerHttpClient(DockerClientConfig config) {
        return new TrackingDockerHttpClient(
            new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build()
        );
    }

    public static DockerClientImpl createDockerClient(DockerClientConfig config) {
        return (DockerClientImpl) DockerClientBuilder.getInstance(config)
            .withDockerHttpClient(createDockerHttpClient(config))
            .build();
    }

    @Rule
    public DockerRule dockerRule = new DockerRule();

    @Rule
    public DockerHttpClientLeakDetector leakDetector = new DockerHttpClientLeakDetector();
}
