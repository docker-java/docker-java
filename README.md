[![Join the chat at https://gitter.im/docker-java/docker-java](https://badges.gitter.im/docker-java/docker-java.svg)](https://gitter.im/docker-java/docker-java?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.docker-java/docker-java.svg)](https://mvnrepository.com/artifact/com.github.docker-java/docker-java)
[![Bintray](https://api.bintray.com/packages/kostyasha/maven/com.github.docker-java%3Adocker-java/images/download.svg)](https://bintray.com/kostyasha/maven/com.github.docker-java%3Adocker-java/_latestVersion) 
[![Build Status](https://travis-ci.org/docker-java/docker-java.svg?branch=master)](https://travis-ci.org/docker-java/docker-java)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/9177/badge.svg?flat=1)](https://scan.coverity.com/projects/9177)
[![codecov.io](http://codecov.io/github/docker-java/docker-java/coverage.svg?branch=master)](http://codecov.io/github/docker-java/docker-java?branch=master)
[![License](http://img.shields.io/:license-apache-blue.svg?style=flat)](https://github.com/docker-java/docker-java/blob/master/LICENSE)

<!--[![Circle CI](https://circleci.com/gh/docker-java/docker-java.svg?style=svg)](https://circleci.com/gh/docker-java/docker-java)-->
# docker-java 

Java API client for [Docker](http://docs.docker.io/ "Docker")

[Changelog](https://github.com/docker-java/docker-java/blob/master/CHANGELOG.md)<br/>
[Wiki](https://github.com/docker-java/docker-java/wiki)

## Build with Maven

###### Prerequisites:

* Java min 1.8
* Maven 3

Build and run integration tests as follows:

    $ mvn clean install

If you do not have access to a Docker server or just want to execute the build quickly, you can run the build without the integration tests:

    $ mvn clean install -DskipITs

By default the docker engine is using local UNIX sockets for communication with the docker CLI so docker-java
client also uses UNIX domain sockets to connect to the docker daemon by default. To make the docker daemon listening on a TCP (http/https) port you have to configure it by setting the DOCKER_OPTS environment variable to something like the following: 

    DOCKER_OPTS="-H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock"
    
More details about setting up Docker Engine can be found in the official documentation: https://docs.docker.com/engine/admin/

To force docker-java to use TCP (http) configure the following (see [Configuration](https://github.com/docker-java/docker-java#configuration) for details):

    DOCKER_HOST=tcp://127.0.0.1:2375
    
For secure tls (https) communication:   

    DOCKER_HOST=tcp://127.0.0.1:2376
    DOCKER_TLS_VERIFY=1
    DOCKER_CERT_PATH=/Users/marcus/.docker/machine/machines/docker-1.11.2

### Latest release version
[Maven repository modules](https://mvnrepository.com/artifact/com.github.docker-java)

Since 3.2.0 project provides 3 transports:
- `docker-java-transport-jersey` (doesn't support streams)
- `docker-java-transport-netty`
- `docker-java-transport-okhttp`

For backward compatibility `docker-java` module keeping dependency only on jersey and netty transports. 

    <dependency>
          <groupId>com.github.docker-java</groupId>
          <artifactId>docker-java</artifactId>
          <!-- use latest version https://github.com/docker-java/docker-java/releases -->
          <version>3.X.Y</version>
    </dependency>
    
### Latest development version
May contain new features while they are not released.

You can find the latest development version including javadoc and source files on [Sonatypes OSS repository](https://oss.sonatype.org/content/groups/public/com/github/docker-java/docker-java/).

    <dependency>
          <groupId>com.github.docker-java</groupId>
          <artifactId>docker-java</artifactId>
          <version>3.X.Y-SNAPSHOT</version>
    </dependency>
    

## Documentation

For code examples, please look at the [Wiki](https://github.com/docker-java/docker-java/wiki) or [Test cases](https://github.com/docker-java/docker-java/tree/master/docker-java/src/test/java/com/github/dockerjava/core/command "Test cases")

## Configuration

There are a couple of configuration items, all of which have sensible defaults:

* `DOCKER_HOST` The Docker Host URL, e.g. `tcp://localhost:2376` or `unix:///var/run/docker.sock`
* `DOCKER_TLS_VERIFY` enable/disable TLS verification (switch between `http` and `https` protocol)
* `DOCKER_CERT_PATH` Path to the certificates needed for TLS verification
* `DOCKER_CONFIG` Path for additional docker configuration files (like `.dockercfg`)
* `api.version` The API version, e.g. `1.23`.
* `registry.url` Your registry's address.
* `registry.username` Your registry username (required to push containers).
* `registry.password` Your registry password.
* `registry.email` Your registry email.

There are three ways to configure, in descending order of precedence:

#### Programmatic:
In your application, e.g.

    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost("tcp://my-docker-host.tld:2376")
        .withDockerTlsVerify(true)
        .withDockerCertPath("/home/user/.docker/certs")
        .withDockerConfig("/home/user/.docker")
        .withApiVersion("1.30") // optional
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
    api.version=1.23
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

##### File System

In `$HOME/.docker-java.properties`

##### Class Path

In the class path at `/docker-java.properties`
