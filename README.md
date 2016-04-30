[![Build Status](https://travis-ci.org/docker-java/docker-java.svg?branch=master)](https://travis-ci.org/docker-java/docker-java)
<!--[![Circle CI](https://circleci.com/gh/docker-java/docker-java.svg?style=svg)](https://circleci.com/gh/docker-java/docker-java)-->
# docker-java 

Java API client for [Docker](http://docs.docker.io/ "Docker")

<b>The current implementation is based on Jersey 2.x and therefore classpath incompatible with older Jersey 1.x dependent libraries!</b>

Developer forum for [docker-java](https://groups.google.com/forum/?#!forum/docker-java-dev "docker-java")

## Build with Maven

###### Prerequisites:

* Java 1.7
* Maven 3.0.5

If you need SSL, then you'll need to put your `*.pem` file into `~/.docker/`, if you're using boot2docker, do this: 
 
    $ ln -s /Users/alex.collins/.boot2docker/certs/boot2docker-vm .docker

Build and run integration tests as follows:

    $ mvn clean install

If you do not have access to a Docker server or just want to execute the build quickly, you can run the build without the integration tests:

    $ mvn clean install -DskipITs

By default Docker server is using UNIX sockets for communication with the Docker client, however docker-java
client uses TCP/IP to connect to the Docker server by default, so you will need to make sure that your Docker server is
listening on TCP port. To allow Docker server to use TCP add the following line to /etc/default/docker

    DOCKER_OPTS="-H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock"

However you can force docker-java to use UNIX socket communication by configure the following (see [Configuration](.#Configuration) for details):

    DOCKER_HOST=unix:///var/run/docker.sock
    DOCKER_TLS_VERIFY=0

More details about setting up Docker server can be found in official documentation: http://docs.docker.io/en/latest/use/basics/

Now make sure that docker is up:

    $ docker -H tcp://127.0.0.1:2375 version

    Client version: 0.8.0
	Go version (client): go1.2
	Git commit (client): cc3a8c8
	Server version: 1.2.0
	Git commit (server): fa7b24f
	Go version (server): go1.3.1

Run build without integration tests:

    $ mvn clean install -DskipITs

## Docker-Java maven dependencies

### Latest release version
Supports a subset of the Docker Remote API [v1.19](https://github.com/docker/docker/blob/master/docs/reference/api/docker_remote_api_v1.19.md), Docker Server version 1.7.x

    <dependency>
          <groupId>com.github.docker-java</groupId>
          <artifactId>docker-java</artifactId>
          <version>2.2.3</version>
    </dependency>
    
### Latest release candidate
Supports a subset of the Docker Remote API [v1.22](https://github.com/docker/docker/blob/master/docs/reference/api/docker_remote_api_v1.22.md), Docker Server version 1.10.x

    <dependency>
          <groupId>com.github.docker-java</groupId>
          <artifactId>docker-java</artifactId>
          <version>3.0.0-RC5</version>
    </dependency>
    
### Latest development version
Supports a subset of the Docker Remote API [v1.22](https://github.com/docker/docker/blob/master/docs/reference/api/docker_remote_api_v1.22.md), Docker Server version 1.10.x

You can find the latest development version including javadoc and source files on [Sonatypes OSS repository](https://oss.sonatype.org/content/groups/public/com/github/docker-java/docker-java/).

    <dependency>
          <groupId>com.github.docker-java</groupId>
          <artifactId>docker-java</artifactId>
          <version>3.0.0-SNAPSHOT</version>
    </dependency>
    

## Documentation

For code examples, please look at the [Wiki](https://github.com/docker-java/docker-java/wiki) or [Test cases](https://github.com/docker-java/docker-java/tree/master/src/test/java/com/github/dockerjava/core/command "Test cases")

## Configuration

There are a couple of configuration items, all of which have sensible defaults:

* `DOCKER_HOST` The Docker Host URL, e.g. `tcp://localhost:2376` or `unix:///var/run/docker.sock`
* `DOCKER_TLS_VERIFY` enable/disable TLS verification (switch between `http` and `https` protocol)
* `DOCKER_CERT_PATH` Path to the certificates needed for TLS verification
* `DOCKER_CONFIG` Path for additional docker configuration files (like `.dockercfg`)
* `api.version` The API version, e.g. `1.21`.
* `registry.url` Your registry's address.
* `registry.username` Your registry username (required to push containers).
* `registry.password` Your registry password.
* `registry.email` Your registry email.

There are three ways to configure, in descending order of precedence:

#### Programmatic:
In your application, e.g.

    DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost("tcp://my-docker-host.tld:2376")
        .withDockerTlsVerify(true)
        .withDockerCertPath("/home/user/.docker/certs")
        .withDockerConfig("/home/user/.docker")
        .withApiVersion("1.21")
        .withRegistryUrl("https://index.docker.io/v1/")
        .withRegistryUsername("dockeruser")
        .withRegistryPassword("ilovedocker")
        .withRegistryEmail("dockeruser@github.com")
        .build();
    DockerClient docker = DockerClientBuilder.getInstance(config).build();

#### Properties (docker-java.properties)

    DOCKER_HOST=tcp://localhost:2376
    DOCKER_TLS_VERIFY=1
    DOCKER_CERT_PATH=/home/user/.docker/certs
    DOCKER_CONFIG=/home/user/.docker
    api.version=1.21
    registry.url=https://index.docker.io/v1/
    registry.username=dockeruser
    registry.password=ilovedocker
    registry.email=dockeruser@github.com

##### System Properties:

    java -Dregistry.username=dockeruser pkg.Main

##### System Environment

    export DOCKER_URL=tcp://localhost:2376
    export DOCKER_TLS_VERIFY=1
    export DOCKER_CERT_PATH=/home/user/.docker/certs
    export DOCKER_CONFIG=/home/user/.docker

##### File System

In `$HOME/.docker-java.properties`

##### Class Path

In the class path at `/docker-java.properties`
    
