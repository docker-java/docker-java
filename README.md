# docker-java 

Java API client for [Docker](http://docs.docker.io/ "Docker")

Supports a subset of the Docker Client API v1.14, Docker Server version 1.2.0

Developer forum for [docker-java](https://groups.google.com/forum/?hl=de#!forum/docker-java-dev "docker-java")

## Build with Maven

###### Prerequisites:

* Java 1.6+
* Maven 3.0.5
* Docker daemon running

Maven may run tests during build process but tests are disabled by default. The tests are using a localhost instance of Docker, make sure that you have Docker running for tests to work. To run the tests you have to provide your https://www.docker.io/account/login/ information:

    $ mvn clean install -DskipTests=false -Ddocker.io.username=... -Ddocker.io.password=... -Ddocker.io.email=...

By default Docker server is using UNIX sockets for communication with the Docker client, however docker-java
client uses TCP/IP to connect to the Docker server, so you will need to make sure that your Docker server is
listening on TCP port. To allow Docker server to use TCP add the following line to /etc/default/docker

    DOCKER_OPTS="-H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock"

More details setting up docket server can be found in official documentation: http://docs.docker.io/en/latest/use/basics/

Now make sure that docker is up:

    $ docker -H tcp://127.0.0.1:2375 version

    Client version: 0.8.0
	Go version (client): go1.2
	Git commit (client): cc3a8c8
	Server version: 1.2.0
	Git commit (server): fa7b24f
	Go version (server): go1.3.1

Run build with tests:

    $ mvn clean install -DskipTests=false

## Docker-Java maven dependencies

### Latest release version

    <dependency>
          <groupId>com.github.docker-java</groupId>
          <artifactId>docker-java</artifactId>
          <version>0.10.1</version>
    </dependency>

### Latest SNAPSHOT version

    <dependency>
          <groupId>com.github.docker-java</groupId>
          <artifactId>docker-java</artifactId>
          <version>0.10.2-SNAPSHOT</version>
    </dependency>

Latest SNAPSHOT is published to maven repo: https://oss.sonatype.org/content/groups/public via ![Build on CloudBees](http://cloudbees.prod.acquia-sites.com/sites/default/files/styles/large/public/Button-Powered-by-CB.png?itok=uMDWINfY)

## Documentation

For code examples, please look at the [Wiki](https://github.com/docker-java/docker-java/wiki) or [Test cases](https://github.com/docker-java/docker-java/tree/master/src/test/java/com/github/dockerjava/core/command "Test cases")

## Configuration

There are a couple of configuration items, all of which have sensible defaults:

* `url` The Docker URL, e.g. `http://localhost:2375`.
* `version` The API version, e.g. `1.14`.
* `username` Your repository username (required to push containers).
* `password` Your repository password.
* `email` Your repository email.

There are three ways to configure, in descending order of precedence:

##### Programatic:
In your application, e.g.

    DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder();
    config.withVersion("1.14");
    config.withUsername("dockeruser");
    config.withPassword("ilovedocker");
    config.withEmail("dockeruser@github.com);
    DockerClient docker = DockerClientBuilder.getInstance( config ).build();

##### System Properties:
E.g.

    java -Ddocker.io.username=kpelykh pkg.Main

##### File System  
In `$HOME/.docker.io.properties`, e.g.:

    docker.io.username=dockeruser

##### Class Path
In the class path at `/docker.io.properties`, e.g.:

    docker.io.url=http://localhost:2375
    docker.io.version=1.13
