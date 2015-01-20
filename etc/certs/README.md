# Creating Certificates for Docker

## Warning
> These certificates are only meant for integration tests on CI environments (like circleCI). Do not use them for any real machine.
> Since all keys are publicly available anybody could gain root access to your machine.

### 1. Create the certificate files
There is an [excellent guide](https://docs.docker.com/articles/https/) on the official docker homepage.
This document contains the log on how the certificates in this folder were created.
It differs slightly form the official guide.
 - Certificates are valid for 10 years instead of 1 year.
 - Certificates use v3_req extension to support both `127.0.0.1` and `localhost` (see config file [server-cert.txt](server-cert.txt)).

```
$ cd ~
```

```
$ mkdir .docker
```

```
$ cd .docker
```

```
$ echo 01 > ca.srl
```

```
$ openssl genrsa -des3 -out ca-key.pem 2048
Generating RSA private key, 2048 bit long modulus
............................................+++
..................+++
e is 65537 (0x10001)
Enter pass phrase for ca-key.pem: docker-java
Verifying - Enter pass phrase for ca-key.pem: docker-java
```

```
$ openssl req -new -x509 -days 3650 -key ca-key.pem -out ca.pem
Enter pass phrase for ca-key.pem: docker-java
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:
State or Province Name (full name) [Some-State]:
Locality Name (eg, city) []:
Organization Name (eg, company) [Internet Widgits Pty Ltd]: docker-java
Organizational Unit Name (eg, section) []:
Common Name (e.g. server FQDN or YOUR name) []:
Email Address []:
```

```
$ openssl genrsa -des3 -out server-key.pem 2048
Generating RSA private key, 2048 bit long modulus
..........+++
.........+++
e is 65537 (0x10001)
Enter pass phrase for server-key.pem: docker-java
Verifying - Enter pass phrase for server-key.pem: docker-java
```

```
$ openssl req -new -key server-key.pem -out server.csr -config server-cert.txt
Enter pass phrase for server-key.pem: docker-java
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
TypeCommonNameHere []: localhost
```

```
$ openssl x509 -req -days 3650 -in server.csr -CA ca.pem -CAkey ca-key.pem -out server-cert.pem -extensions v3_req -extfile server-cert.txt
Signature ok
subject=/CN=localhost
Getting CA Private Key
Enter pass phrase for ca-key.pem: docker-java
```

```
$ openssl genrsa -des3 -out key.pem 2048
Generating RSA private key, 2048 bit long modulus
............................................+++
.......................................................+++
e is 65537 (0x10001)
Enter pass phrase for key.pem: docker-java
Verifying - Enter pass phrase for key.pem: docker-java
```

```
$ openssl req -subj '/CN=client' -new -key key.pem -out client.csr
Enter pass phrase for key.pem: docker-java
```

```
$ echo extendedKeyUsage = clientAuth > extfile.cnf
```

```
$ openssl x509 -req -days 3650 -in client.csr -CA ca.pem -CAkey ca-key.pem -out cert.pem -extfile extfile.cnf
Signature ok
subject=/CN=client
Getting CA Private Key
Enter pass phrase for ca-key.pem: docker-java
```

```
$ openssl rsa -in server-key.pem -out server-key.pem
Enter pass phrase for server-key.pem: docker-java
writing RSA key
```

```
$ openssl rsa -in key.pem -out key.pem
Enter pass phrase for key.pem: docker-java
writing RSA key
```

Once you created all the files you can have a look at their content with the following command

```
openssl x509 -in <some-file>.pem -inform pem -noout -text
```

### 2. Configuring the docker daemon
On linux the docker daemon allows to specify options in the file `/etc/default/docker`.
By adding the following line (or modifying an existing line) one can get the docker daemon to listen on both *unix socket* and *https*.
```
DOCKER_OPTS="-H unix:///var/run/docker.sock -H tcp://127.0.0.1:2376 --tlsverify --tlscacert=~/.docker/ca.pem --tlscert=~/.docker/server-cert.pem --tlskey=~/.docker/server-key.pem"
```

### 3. Restart the daemon and test the setup
After changing the daemon options it must be restarted

```
$ sudo service docker restart
```

To test the socket and the https connection:

```
$ docker -H tcp://127.0.0.1:2376 --tlsverify version
Client version: 1.4.1
Client API version: 1.16
Go version (client): go1.3.3
Git commit (client): 5bc2ff8
OS/Arch (client): linux/amd64
Server version: 1.4.1
Server API version: 1.16
Go version (server): go1.3.3
Git commit (server): 5bc2ff8
```

```
$ docker -H unix:///var/run/docker.sock version
Client version: 1.4.1
Client API version: 1.16
Go version (client): go1.3.3
Git commit (client): 5bc2ff8
OS/Arch (client): linux/amd64
Server version: 1.4.1
Server API version: 1.16
Go version (server): go1.3.3
Git commit (server): 5bc2ff8
```
