# Getting Started

## Dependencies

To start using `docker-java` , you need to add at least two dependencies:
1. `com.github.docker-java:docker-java-core` for the `DockerClient`
1. one of `com.github.docker-java:docker-java-transport-*` to communicate with the Docker daemon. See [Available Transports](./transports.md) for more info.

The latest available version: 
[![Maven Central](https://img.shields.io/maven-central/v/com.github.docker-java/docker-java.svg)](https://mvnrepository.com/artifact/com.github.docker-java/docker-java)


## Instantiating a `DockerClientConfig`

You will need an instance of `DockerClientConfig` to tell the library how to access Docker, which credentials to use to pull from Docker registries, etc etc.

The builder is available and allows you to configure every property of the client:
```java
import com.github.dockerjava.core.DockerClientConfig
import com.github.dockerjava.core.DefaultDockerClientConfig
DockerClientConfig standard = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
```

```java
import com.github.dockerjava.core.DockerClientConfig
import com.github.dockerjava.core.DefaultDockerClientConfig

DockerClientConfig custom = DefaultDockerClientConfig.createDefaultConfigBuilder()
    .withDockerHost("tcp://docker.somewhere.tld:2376")
    .withDockerTlsVerify(true)
    .withDockerCertPath("/home/user/.docker")
    .withRegistryUsername(registryUser)
    .withRegistryPassword(registryPass)
    .withRegistryEmail(registryMail)
    .withRegistryUrl(registryUrl)
    .build();
```

Here you can tune registry auth, DOCKER_HOST and other options.

There are a couple of configuration items, all of which have sensible defaults:

* `DOCKER_HOST` The Docker Host URL, e.g. `tcp://localhost:2376` or `unix:///var/run/docker.sock`
* `DOCKER_TLS_VERIFY` enable/disable TLS verification (switch between `http` and `https` protocol)
* `DOCKER_CERT_PATH` Path to the certificates needed for TLS verification
* `DOCKER_CONFIG` Path for additional docker configuration files (like `.dockercfg`)
* `api.version` The API version, e.g. `1.23`. Leave unset to use the daemon's default, or enable
  `api.version.auto.negotiation` to have the client pick the highest mutually-supported version at startup.
* `api.version.auto.negotiation` When `true` (or `1`), the client calls `GET /version` once at construction
  and pins itself to `min(daemon's API version, latest version supported by docker-java)`. Default `false`.
  An explicit `api.version` always wins over auto-negotiation.
* `registry.url` Your registry's address.
* `registry.username` Your registry username (required to push containers).
* `registry.password` Your registry password.
* `registry.email` Your registry email.

There are three ways to configure, in descending order of precedence:

##### Properties (docker-java.properties)

    DOCKER_HOST=tcp://localhost:2376
    DOCKER_TLS_VERIFY=1
    DOCKER_CERT_PATH=/home/user/.docker/certs
    DOCKER_CONFIG=/home/user/.docker
    api.version=1.23
    api.version.auto.negotiation=false
    registry.url=https://index.docker.io/v1/
    registry.username=dockeruser
    registry.password=ilovedocker
    registry.email=dockeruser@github.com

##### System Properties:

    java -DDOCKER_HOST=tcp://localhost:2375 -Dregistry.username=dockeruser pkg.Main

##### System Environment

    export DOCKER_HOST=tcp://localhost:2376
    export DOCKER_TLS_VERIFY=1
    export DOCKER_CERT_PATH=/home/user/.docker/certs
    export DOCKER_CONFIG=/home/user/.docker
    export DOCKER_API_VERSION_AUTO_NEGOTIATION=1

##### File System

In `$HOME/.docker-java.properties`

##### Class Path

In the class path at `/docker-java.properties`

### API version auto-negotiation

By default, `docker-java` either talks to the daemon using whatever API version is configured
(`api.version` / `withApiVersion(...)`), or — if none is configured — lets the daemon choose. The client
itself doesn't know what the daemon supports.

Opting in to auto-negotiation makes the client query `GET /version` once at construction time and pin
itself to `min(daemon's reported API version, latest version known to docker-java)`. This is the same
behaviour `moby/client.NegotiateAPIVersion` provides in the Go client.

```java
DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
    .withApiVersionAutoNegotiation(true)
    .build();

DockerClient client = DockerClientImpl.getInstance(config, httpClient);
```

Equivalent env var: `DOCKER_API_VERSION_AUTO_NEGOTIATION=1`. Equivalent property: `api.version.auto.negotiation=true`.

Precedence: an explicit `withApiVersion(...)` (or `api.version` property / env var) always wins, even when
auto-negotiation is enabled. If the daemon's reported minimum is higher than the latest version known to
docker-java, the daemon minimum is used and a warning is logged.

### Jackson

Should you need to customize the Jackson's `ObjectMapper` used by `docker-java`, you can create your own `DockerClientConfig` and override `DockerClientConfig#getObjectMapper()`.

## Instantiating a `DockerHttpClient`
Once you decided which transport to use, you will need to instantiate an HTTP client:
```java
DockerClientConfig config = ...;

DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
    .dockerHost(config.getDockerHost())
    .sslConfig(config.getSSLConfig())
    .maxConnections(100)
    .connectionTimeout(Duration.ofSeconds(30))
    .responseTimeout(Duration.ofSeconds(45))
    .build();
```

Please refer to selected transport's builder for other available configuration options (like timeouts).

Once you have an HTTP client, you can make raw requests to the Docker daemon directly:
```java
Request request = Request.builder()
    .method(Request.Method.GET)
    .path("/_ping")
    .build();

try (Response response = httpClient.execute(request)) {
    assertThat(response.getStatusCode(), equalTo(200));
    assertThat(IOUtils.toString(response.getBody()), equalTo("OK"));
}
```

## Instantiating a `DockerClient`

To get an instance of `DockerClient`, you need to pass both `DockerClientConfig` and `DockerHttpClient`:
```java
DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
```

Once you have it, you can start executing Docker commands:
```java
dockerClient.pingCmd().exec();
```
