# docker-java-transport-ssh

Docker client implementation which uses [jsch](http://www.jcraft.com/jsch/) library, a java ssh implementation, to connect to the remote 
docker host via ssh.

While native docker cli supports ssh connections since Host docker version 18.09 [<sup>1</sup>](#1), with different options we can also make 
it work for older versions. This library opens the ssh connection and then forwards the docker daemon socket to make it available to the http client.

The ssh connection configuration relies on basic [ssh config file](https://www.ssh.com/ssh/config/) in ~/.ssh/config.

## dockerd configurations

On the remote host, one can connect to the docker daemon in several ways:

* `docker system dial-stdio`
* `unix:///var/run/docker.sock` (default on linux) https://docs.docker.com/engine/reference/commandline/dockerd/#daemon-socket-option
* `npipe:////./pipe/docker_engine` (default on Windows) https://docs.docker.com/docker-for-windows/faqs/#how-do-i-connect-to-the-remote-docker-engine-api
* `unix:///var/run/docker.sock` (default on macos) https://docs.docker.com/docker-for-mac/faqs/#how-do-i-connect-to-the-remote-docker-engine-api
* tcp 2375
* tcp with TLS 

## limitations

__jsch__

Since jsch libary from jcraft does not support socket forwarding, a [fork of jsch](https://github.com/mwiede/jsch) is used. 

__windows__
 
Since forwarding socket of windows host is not supported, there is the workaround of starting socat to forward the docker socket to a local tcp port.
 
Compare OpenSSH tickets:      
 * https://github.com/PowerShell/Win32-OpenSSH/issues/435
 * https://github.com/PowerShell/openssh-portable/pull/433

## connection variants:

By setting flags in [SSHDockerConfig](src\main\java\com\github\dockerjava\jsch\SSHDockerConfig.java), one can control how the connection is made.

* docker system dial-stdio (default)
* direct-streamlocal
* direct-tcpip
* socat

## references

<a class="anchor" id="1">[1]</a> docker ssh support https://github.com/docker/cli/pull/1014
