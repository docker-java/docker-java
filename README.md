# docker-java

Java API client for [Docker](http://docs.docker.io/ "Docker")

Supports a subset of the Docker Client API v1.12, Docker Server version 1.0

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
    Server version: 1.0.0
    Git commit (server): 63fe64c
    Go version (server): go1.2.1

Run build with tests:

    $ mvn clean install -DskipTests=false

## Docker-Java maven dependency:

    <dependency>
          <groupId>com.github.docker-java</groupId>
          <artifactId>docker-java</artifactId>
          <version>0.9.0-SNAPSHOT</version>
    </dependency>

Latest SNAPSHOT is available from maven repo: https://oss.sonatype.org/content/groups/public   

## Example code snippets:

    DockerClient dockerClient = new DockerClient("http://localhost:2375");

###### Get Docker info:

    Info info = dockerClient.infoCmd().exec();
    System.out.print(info);

###### Search Docker repository:

    List<SearchItem> dockerSearch = dockerClient.searchImagesCmd("busybox").exec();
    System.out.println("Search returned" + dockerSearch.toString());

###### Create new Docker container, wait for its start and stop it:

    ContainerCreateResponse container = dockerClient.createContainerCmd("busybox").withCmd("touch", "/test").exec();

    dockerClient.startContainerCmd(container.id).exec();

    dockerClient.waitContainerCmd(container.id).exec();

    dockerClient.stopContainerCmd(container.id).exec();


##### Support for UNIX sockets:

    Support for UNIX socket should appear in docker-java pretty soon. I'm working on its integration.

##### Docker Builder:

To use Docker Builder, as described on page http://docs.docker.io/en/latest/use/builder/,
user dockerClient.build(baseDir), where baseDir is a path to folder containing Dockerfile.


    File baseDir = new File("~/kpelykh/docker/netcat");

    ClientResponse response = dockerClient.buildImageCmd(baseDir).exec();

    StringWriter logwriter = new StringWriter();

    try {
        LineIterator itr = IOUtils.lineIterator(response.getEntityInputStream(), "UTF-8");
        while (itr.hasNext()) {
            String line = itr.next();
            logwriter.write(line);
            LOG.info(line);
        }
    } finally {
        IOUtils.closeQuietly(response.getEntityInputStream());
    }



For additional examples, please look at [Test cases](https://github.com/docker-java/docker-java/tree/master/src/test/java/com/github/dockerjava/client/command "Test cases")

## Configuration

There are a couple of configuration items, all of which have sensible defaults:

* `url` The Docker URL, e.g. `http://localhost:2375`.
* `version` The API version, e.g. `1.12`.
* `username` Your repository username (required to push containers).
* `password` Your repository password.
* `email` Your repository email.

There are three ways to configure, in descending order of precedence:

##### Programatic:
In your application, e.g.

    DockerClient docker = new DockerClient("http://localhost:2375");
    docker.setCredentials("dockeruser", "ilovedocker", "dockeruser@github.com");`

##### System Properties:
E.g.

    java -Ddocker.io.username=kpelykh pkg.Main

##### File System  
In `$HOME/.docker.io.properties`, e.g.:

    docker.io.username=dockeruser

##### Class Path
In the class path at `/docker.io.properties`, e.g.:

    docker.io.url=http://localhost:2375
    docker.io.version=1.11
