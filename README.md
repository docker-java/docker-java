# docker-java 

Java API client for [Docker](http://docs.docker.io/ "Docker")

Supports a subset of the Docker Client API v1.15, Docker Server version 1.3.0

<b>The current implementation is based on Jersey 2.x and therefore classpath incompatible with older Jersey 1.x dependent libraries!</b>

Developer forum for [docker-java](https://groups.google.com/forum/?hl=de#!forum/docker-java-dev "docker-java")

## Build with Maven

###### Prerequisites:

* Java 1.6
* Maven 3.0.5
* Docker daemon running

The Maven build includes integration tests which are using a localhost instance of Docker and require manual setup. Make sure you have a local Docker daemon running and then provide your https://registry.hub.docker.com/account/login/ information via system properties:

    $ mvn clean install -Ddocker.io.username=... -Ddocker.io.password=... -Ddocker.io.email=...

_If your Docker server is remote, add its URL like this: `-Ddocker.io.url=https://...:2376`._

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
          <version>0.10.2</version>
    </dependency>

### Latest SNAPSHOT version

    <dependency>
          <groupId>com.github.docker-java</groupId>
          <artifactId>docker-java</artifactId>
          <version>0.10.3-SNAPSHOT</version>
    </dependency>

Latest SNAPSHOT is published to maven repo: https://oss.sonatype.org/content/groups/public via ![Build on CloudBees](http://cloudbees.prod.acquia-sites.com/sites/default/files/styles/large/public/Button-Powered-by-CB.png?itok=uMDWINfY)

## Documentation

For code examples, please look at the [Wiki](https://github.com/docker-java/docker-java/wiki) or [Test cases](https://github.com/docker-java/docker-java/tree/master/src/test/java/com/github/dockerjava/core/command "Test cases")

## Configuration

There are a couple of configuration items, all of which have sensible defaults:

* `url` The Docker URL, e.g. `https://localhost:2376`.
* `version` The API version, e.g. `1.15`.
* `username` Your repository username (required to push containers).
* `password` Your repository password.
* `email` Your repository email.
* `dockerCertPath` Path to the docker certs.

There are three ways to configure, in descending order of precedence:

#### Programatic:
In your application, e.g.

    DockerClientConfigBuilder configBuilder = DockerClientConfig.createDefaultConfigBuilder();
    configBuilder.withVersion("1.15");
    configBuilder.withUri("https://my-docker-host.tld:2376");
    configBuilder.withUsername("dockeruser");
    configBuilder.withPassword("ilovedocker");
    configBuilder.withEmail("dockeruser@github.com");
    configBuilder.withDockerCertPath("/home/user/.docker");
    DockerClientConfig config = configBuilder.build();
    DockerClient docker = DockerClientBuilder.getInstance(config).build();

#### Properties

    docker.io.url=https://localhost:2376
    docker.io.version=1.15
    docker.io.username=dockeruser
    docker.io.password=ilovedocker
    docker.io.email=dockeruser@github.com
    docker.io.dockerCertPath=/home/user/.docker


##### System Properties:

    java -Ddocker.io.username=dockeruser pkg.Main

##### File System  

In `$HOME/.docker.io.properties`

##### Class Path

In the class path at `/docker.io.properties`
    
