# Containers

### List Containers
- **List of Running Containers**
```docker ps ``` **equivalent**

```
List<Container> containers = dockerClient.listContainersCmd().exec();
```
- **List of all containers**
```docker ps -a ``` **equivalent**

```
List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
```
### Creating a container

    $ docker run /
	--name learn_postgres /
	-e POSTGRES_PASSWORD=mypass /
	-p 5430:5432/
	-d postgres
**equivalent**


        HostConfig hostConfig = HostConfig.newHostConfig()
                .withPortBindings(new PortBinding(Ports.Binding.bindPort(5432),ExposedPort.tcp(5430)));


        CreateContainerResponse container
                = dockerClient.createContainerCmd("postgres")
                .withName("learn_postgres").withEnv("POSTGRES_PASSWORD=mypass").withHostConfig(hostConfig)
            .exec();
			
##Start,Stop and Kill Container

We can pass name of the container or the ID of the container.

```
dockerClient.startContainerCmd(container.getId()).exec();

dockerClient.stopContainerCmd(container.getId()).exec();

dockerClient.killContainerCmd(container.getId()).exec();
```

## Inspect a container

```
InspectContainerResponse container 
  = dockerClient.inspectContainerCmd(container.getId()).exec();
```
