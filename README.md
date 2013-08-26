# docker-java

Java API client for [Docker](http://docs.docker.io/ "Docker")

Supports Docker Client API v1.3, Docker Server version 0.6.1

## Build with Maven

###### Prerequisites:

* Java 1.6+
* Maven 3.0.5
* Docker daemon running


By default maven will run tests during build process. Tests are using localhost instance of Docker, make sure that
you have Docker running, or the tests.

*Since version 0.6, Docker is using unix socket for communication, however java client works over TCP/IP, so you need to
make sure that your Docker server is listening on TCP/IP port.*

Run docker:

    $ sudo docker -H=tcp://127.0.0.1:4243 -d

Make sure that docker is up:
    
    $docker -H=tcp://127.0.0.1:4243 version
    Client version: 0.6.1
    Server version: 0.6.1
    Git commit: 5105263
    Go version: go1.1.2
    Last stable version: 0.6.1

Run build with tests:

    $ mvn clean install

If you don't have Docker running localy, you can skip tests with -DskipTests flag set to true:

    $ mvn clean install -DskipTests=true

## Docker Java Client usage:

To use Java Docker client, include dependency into your pom.xml:

    <dependency>
          <groupId>com.kpelykh</groupId>
          <artifactId>docker-java</artifactId>
          <version>0.6.1-SNAPSHOT</version>
    </dependency>

*Currently Docker Java client is not available in Maven Central, so you will need to install it to a local
repository, before you can use it in your projects.*
    
## Example code snippets:

    DockerClient dockerClient = new DockerClient("http://localhost:4243");


###### Get Docker info:

    Info info = dockerClient.info();
    System.out.print(info);
    
###### Search Docker repository:

    List<SearchItem> dockerSearch = dockerClient.search("busybox");
    System.out.println("Search returned" + dockerSearch.toString());
      
###### Create new Docker container, wait for its start and stop it:

    ContainerConfig containerConfig = new ContainerConfig();
    containerConfig.setImage("busybox");
    containerConfig.setCmd(new String[] {"touch", "/test"});
    ContainerCreateResponse container = dockerClient.createContainer(containerConfig);

    dockerClient.startContainer(container.id);

    dockerClient.waitContainer(container.id);

    dockerClient.stopContainer(container.id);
    
    
##### Docker Builder:

To use Docker Builder, as described on page http://docs.docker.io/en/latest/use/builder/,
user dockerClient.build(baseDir), where baseDir is a path to folder containing Dockerfile.


    File baseDir = new File("~/kpelykh/docker/netcat");

    ClientResponse response = dockerClient.build(baseDir);

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



For additional examples, please look at [DockerClientTest.java](https://github.com/kpelykh/docker-java/blob/master/src/test/java/com/kpelykh/docker/client/test/DockerClientTest.java "DockerClientTest.java")

