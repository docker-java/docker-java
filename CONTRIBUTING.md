# Build with Maven

#### Prerequisites:

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


# Code Design
  * Model is based on Objects and not primitives that allows nullify requests and have null values for data
  that wasn't provided by docker daemon.
  * For null safeness findbugs annotations are used.
  ** Every method that may return `null` (and we are unsure in any fields as docker daemon may change something)
     should be annotated with `@CheckForNull` return qualifier from `javax.annotation` package.
  ** Methods that can't return `null` must be annotated with `@Nonnull`.
  ** The same for Arguments.
  ** `@Nullable` must be used only for changing inherited (other typed) qualifier.
  * Setters in builder style must be prefixed with `withXX`.
  * All classes should provide `toString()` `equals()` and `hashCode()` defined methods.
  * Javadocs
  ** Provide full information on field:
  *** For models define API version with `@since {@link RemoteApiVersion#VERSION_1_X}`.
  ** getters/setters should refernce to field `@see #$field`.
  * If it is `Serializable` it shall have a `serialVersionUID` field. Unless code has shipped to users, the initial value of the `serialVersionUID` field shall be `1L`.

# Coding style
  * Some initial styling already enforced with checkstyle. Please aim for consistency with the existing code.

# Testing
  * Unit tests for serder (serialization-deserialization).
  * Integration tests for commands.
  * If model object has builders, then fill it with data and compare by `equals()` with expected response
  from docker daemon. If failed, then some fields mappings are wrong.