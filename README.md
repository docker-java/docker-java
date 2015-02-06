[![Build Status](https://travis-ci.org/docker-java/docker-java.svg?branch=master)](https://travis-ci.org/docker-java/docker-java)
# docker-java 

Java API client for [Docker](http://docs.docker.io/ "Docker")

Supports a subset of the Docker Client API v1.16, Docker Server version 1.4.1

<b>The current implementation is based on Jersey 2.x and therefore classpath incompatible with older Jersey 1.x dependent libraries!</b>

Developer forum for [docker-java](https://groups.google.com/forum/?hl=de#!forum/docker-java-dev "docker-java")

## Build with Maven

###### Prerequisites:

* Java 1.7
* Maven 3.0.5
* Docker daemon running

If you need SSL, then you'll need to put your `*.pem` file into `~/.docker/`, if you're using boot2docker, do this: 
 
    $ ln -s /Users/alex.collins/.boot2docker/certs/boot2docker-vm .docker

Build and run integration tests as follows:

    $ mvn clean install

If you do not have access to a Docker server or just want to execute the build quickly, you can run the build without the integration tests:

    $ mvn clean install -DskipITs

By default Docker server is using UNIX sockets for communication with the Docker client, however docker-java
client uses TCP/IP to connect to the Docker server, so you will need to make sure that your Docker server is
listening on TCP port. To allow Docker server to use TCP add the following line to /etc/default/docker

    DOCKER_OPTS="-H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock"

More details setting up Docker server can be found in official documentation: http://docs.docker.io/en/latest/use/basics/

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

    <dependency>
          <groupId>com.github.docker-java</groupId>
          <artifactId>docker-java</artifactId>
          <version>0.10.5</version>
    </dependency>

### Latest SNAPSHOT version
You can find the latest SNAPSHOT version including javadoc and source files on [Sonatypes OSS repository](https://oss.sonatype.org/content/groups/public/com/github/docker-java/docker-java/).


    <dependency>
          <groupId>com.github.docker-java</groupId>
          <artifactId>docker-java</artifactId>
          <version>1.0.0-SNAPSHOT</version>
    </dependency>

## Documentation

For code examples, please look at the [Wiki](https://github.com/docker-java/docker-java/wiki) or [Test cases](https://github.com/docker-java/docker-java/tree/master/src/test/java/com/github/dockerjava/core/command "Test cases")

## Configuration

There are a couple of configuration items, all of which have sensible defaults:

* `url` The Docker URL, e.g. `https://localhost:2376`.
* `version` The API version, e.g. `1.16`.
* `username` Your registry username (required to push containers).
* `password` Your registry password.
* `email` Your registry email.
* `serverAddress` Your registry's address.
* `dockerCertPath` Path to the docker certs.

There are three ways to configure, in descending order of precedence:

#### Programmatic:
In your application, e.g.

    DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
        .withVersion("1.16")
        .withUri("https://my-docker-host.tld:2376")
        .withUsername("dockeruser")
        .withPassword("ilovedocker")
        .withEmail("dockeruser@github.com")
        .withServerAddress("https://index.docker.io/v1/")
        .withDockerCertPath("/home/user/.docker")
        .build();
    DockerClient docker = DockerClientBuilder.getInstance(config).build();

#### Properties

    docker.io.url=https://localhost:2376
    docker.io.version=1.16
    docker.io.username=dockeruser
    docker.io.password=ilovedocker
    docker.io.email=dockeruser@github.com
    docker.io.serverAddress=https://index.docker.io/v1/
    docker.io.dockerCertPath=/home/user/.docker


##### System Properties:

    java -Ddocker.io.username=dockeruser pkg.Main

##### System Environment

    export DOCKER_URL=http://localhost:2376

Note: we also auto-detect defaults. If you use `DOCKER_HOST` we use that value, and if `DOCKER_CERT_PATH` or `DOCKER_TLS_VERIFY=1` is set, we switch to SSL.

##### File System

In `$HOME/.docker.io.properties`

##### Class Path

In the class path at `/docker.io.properties`
    
