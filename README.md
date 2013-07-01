# docker-java

Java API client for Docker (based on docker-py)

## Build with Maven

###### Prerequisites:

* Java 1.6+
* Maven 3.0.5
* Docker daemon running


By default maven will run tests during build process. Tests are using localhost instance of Docker, make sure that
you have Docker running, or the tests.

Run docker:

    $ sudo docker -d

Make sure that docker is up:
    
    $ docker version    
    Client version: 0.4.1
    Server version: 0.4.1
    Go version: go1.1

Run build with tests:

    $ mvn clean install

If you don't have Docker running localy, you can skip tests with -DskipTests flag set to true:

    $ mvn clean install -DskipTests=true

## Docker Java Client usage:

To use Java Docker client, include dependency into your pom.xml:

    <dependency>
          <groupId>com.kpelykh</groupId>
          <artifactId>docker-java</artifactId>
          <version>1.0-SNAPSHOT</version>
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

    ContainerConfig containerConfig =
                new ContainerConfig.Builder("busybox")
                        .cmd(new String[]{"echo"}).build();
    ContainerCreateResponse container = dockerClient.createContainer(containerConfig);
    dockerClient.startContainer(container.id);

    dockerClient.waitContainer(container.id);

    dockerClient.stopContainer(container.id);
    
    
##### Container Builder:

    ContainerConfig containerConfig =
            new ContainerConfig.Builder("busybox")
                    .cmd(new String[] {"true"})
                    .hostName(hostname)
                    .user(user)
                    .stdinOpen(stdinOpen)
                    .tty(tty)
                    .memoryLimit(memLimit)
                    .portSpecs(ports)
                    .env(environment)
                    .dns(dns)
                    .volumes(volumes)
                    .volumesFrom(volumesFrom)
                    .build();
    ContainerCreateResponse container = dockerClient.createContainer(containerConfig);
    
    
For additional examples, please look at [DockerClientTest.java](https://github.com/kpelykh/docker-java/blob/master/src/test/java/com/kpelykh/docker/client/test/DockerClientTest.java "DockerClientTest.java")


## TODO

Currently the following APIs are missing: 

export, history, login, push, tag