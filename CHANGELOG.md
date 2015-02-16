Change Log
===

docker-java-1.0.0-SNAPSHOT
---
 * [#148](https://github.com/docker-java/docker-java/pull/148) Save image functionality
 * [#142](https://github.com/docker-java/docker-java/pull/142) Reduce Logging Level 
 * [#138](https://github.com/docker-java/docker-java/pull/138) Apache CXF interopabilty
 * [#137](https://github.com/docker-java/docker-java/pull/137) Multiple volumesFrom option when creating a container 
 * [#135](https://github.com/docker-java/docker-java/pull/135) Update to latest unix-socket-factory 
 * [#134](https://github.com/docker-java/docker-java/pull/134) Remove Google Guava as dependency 
 * [#131](https://github.com/docker-java/docker-java/pull/128) Utility classes and streamed JSON representations
 * [#128](https://github.com/docker-java/docker-java/pull/128) Allow unauthorized pullImageCmd

docker-java-0.10.5
---
 * [#125](https://github.com/docker-java/docker-java/pull/125) Unixsocket support
 * [#123](https://github.com/docker-java/docker-java/pull/123) support DOCKER_TLS_VERIFY to detect ssl
 * [#121](https://github.com/docker-java/docker-java/pull/121) Implemented support for commands: Exec-start, Exec-create 
 * [#120](https://github.com/docker-java/docker-java/pull/120) Command resource cleanup after command execution
 * [#118](https://github.com/docker-java/docker-java/pull/118) Use chunked encoding when passing the docker image 
 * [#116](https://github.com/docker-java/docker-java/pull/116) Allow SSL configuration from pre-existing keystore to be used
 * [#115](https://github.com/docker-java/docker-java/pull/115) Polish RestartPolicy
 * [#114](https://github.com/docker-java/docker-java/pull/114) Fix CreateContainerCmdImpl.withVolumesFrom()
 * [#111](https://github.com/docker-java/docker-java/pull/111) Allow to send empty messages in StartContainerCmd  

docker-java-0.10.4
---

 * [#109](https://github.com/docker-java/docker-java/pull/109) Support tag in push image command  
 * [#106](https://github.com/docker-java/docker-java/pull/106) Allow to manage Linux capabilities in CreateContainerCmd  
 * [#105](https://github.com/docker-java/docker-java/pull/105) Allow to pass HostConfig when creating a container 
 * [#103](https://github.com/docker-java/docker-java/pull/103) Make GoLangMatchFileFilter work on Windows 
 * [#102](https://github.com/docker-java/docker-java/pull/102) Downgraded jackson-jaxrs dependency version  
 * [#101](https://github.com/docker-java/docker-java/pull/101) list filtered images as described in the remote api docs 
 * [#100](https://github.com/docker-java/docker-java/pull/100) Add support for .dockercfg files to handle auth for push command
 * [#95](https://github.com/docker-java/docker-java/pull/95) Add support for .dockerignore files 
 * [#92](https://github.com/docker-java/docker-java/pull/92) Add travis-ci support
 * [#90](https://github.com/docker-java/docker-java/pull/90) Update DockerClientBuilder.java
 * [#88](https://github.com/docker-java/docker-java/pull/88) Add support for private repositories and pull/push authentication

docker-java-0.10.3
---

 * [#87](https://github.com/docker-java/docker-java/pull/87) Improve adding of port bindings
 * [#83](https://github.com/docker-java/docker-java/pull/83) Loading of custom DockerCmdExecFactory
 * [#81](https://github.com/docker-java/docker-java/pull/81) Env config
 * [#82](https://github.com/docker-java/docker-java/pull/82) Allow multiple port bindings per ExposedPort
 * [#80](https://github.com/docker-java/docker-java/pull/80) explicitly use the latest image version
 * [#79](https://github.com/docker-java/docker-java/pull/79) Move slow tests to integration test phase, enable tests by default
 * [#76](https://github.com/docker-java/docker-java/pull/76) New enum \"InternetProtocol\" for supported IP protocols replaces \"scheme\"
 * [#75](https://github.com/docker-java/docker-java/pull/75) Use ExposedPort.toString() in serialization
 * [#74](https://github.com/docker-java/docker-java/pull/74) Use project.build.sourceEncoding in compiler
 * [#73](https://github.com/docker-java/docker-java/pull/73) Improve parsing and serialization of Link
 * [#70](https://github.com/docker-java/docker-java/pull/70) Improve instantiation and serialization of Bind

docker-java-0.10.2
---

 * [#69](https://github.com/docker-java/docker-java/pull/69) Use canonical form of Docker folder when building TAR files
 * [#68](https://github.com/docker-java/docker-java/pull/68) Set Jersey client CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE
 * [#67](https://github.com/docker-java/docker-java/pull/67) typo in README.md
 * [#65](https://github.com/docker-java/docker-java/pull/65) Added static method udp in ExposedPort
 * [#63](https://github.com/docker-java/docker-java/pull/63) Bind.parse() fails when access mode is specified
 * [#57](https://github.com/docker-java/docker-java/pull/57) Add streaming events API
 * [#59](https://github.com/docker-java/docker-java/pull/59) Update readme to include corrected api example
 * [#2](https://github.com/docker-java/docker-java/pull/2) Move to new maven coordinate com.github.docker-java:docker-java
 * [#56](https://github.com/docker-java/docker-java/pull/56) Update README.md
 * [#58](https://github.com/docker-java/docker-java/pull/58) Code clear and bug fix

docker-java-0.10.1
---

 * [#49](https://github.com/docker-java/docker-java/pull/49) Allow user to check where volume is binded on host
 * [#47](https://github.com/docker-java/docker-java/pull/47) let CompressArchiveUtil preserve executable flags
 * [#46](https://github.com/docker-java/docker-java/pull/46) Fixes to AttachContainerCmd and CreateContainerCmd.

docker-java-0.10.0
---

 * [#45](https://github.com/docker-java/docker-java/pull/45) Fix Issue #44 Adjusting DNS property type to be a String array as specified by the Doc...
 * [#38](https://github.com/docker-java/docker-java/pull/38) Remove special-case for one \":\" from PullCommand
 * [#37](https://github.com/docker-java/docker-java/pull/37) Starts v0.10.0
 * [#35](https://github.com/docker-java/docker-java/pull/35) Exposing the withTTY method for container creation.

docker-java-0.9.1
---

 * [#31](https://github.com/docker-java/docker-java/pull/31) Change VolumesFrom to handle array
 * [#29](https://github.com/docker-java/docker-java/pull/29) Makes Config a public, immutable class with a builder
 * [#22](https://github.com/docker-java/docker-java/pull/22) Fixes for startContainerCmd
 * [#19](https://github.com/docker-java/docker-java/pull/19) Add missing options to BuildCmd and set them to match Docker client.
 * [#16](https://github.com/docker-java/docker-java/pull/16) Build image improvements

docker-java-0.9.0
---

 * [#14](https://github.com/docker-java/docker-java/pull/14) Use RegEx to match ADD command from Dockerfile.
 * [#9](https://github.com/docker-java/docker-java/pull/9) Add a build command accepting a tar as a InputStream, so we can build the Dockerfile TAR on the fly without a temporary folder.

docker-java-0.8.2
---

 * [#2](https://github.com/docker-java/docker-java/pull/2) Move to new maven coordinate com.github.docker-java:docker-java
 * [#1](https://github.com/docker-java/docker-java/pull/1) Merge
 * [#2](https://github.com/docker-java/docker-java/pull/2) Move to new maven coordinate com.github.docker-java:docker-java
 * [#66](https://github.com/docker-java/docker-java/pull/66) Backport the new structure to Jersey 1.18
 * [#65](https://github.com/docker-java/docker-java/pull/65) Added static method udp in ExposedPort
 * [#61](https://github.com/docker-java/docker-java/pull/61) 
 * [#60](https://github.com/docker-java/docker-java/pull/60) Added additional callback methods to EventCallback
 * [#1](https://github.com/docker-java/docker-java/pull/1) Merge
 * [#55](https://github.com/docker-java/docker-java/pull/55) 
 * [#58](https://github.com/docker-java/docker-java/pull/58) Code clear and bug fix
 * [#54](https://github.com/docker-java/docker-java/pull/54) 
 * [#3](https://github.com/docker-java/docker-java/pull/3) 
 * [#2](https://github.com/docker-java/docker-java/pull/2) Move to new maven coordinate com.github.docker-java:docker-java
 * [#1](https://github.com/docker-java/docker-java/pull/1) Merge
 * [#34](https://github.com/docker-java/docker-java/pull/34) 
 * [#36](https://github.com/docker-java/docker-java/pull/36) 
 * [#37](https://github.com/docker-java/docker-java/pull/37) Starts v0.10.0
 * [#32](https://github.com/docker-java/docker-java/pull/32) 

docker-java-0.8.1
---


docker-java-0.8.1
---

 * [#28](https://github.com/docker-java/docker-java/pull/28) Improves use of docker-java in unit tests
 * [#30](https://github.com/docker-java/docker-java/pull/30) Add ping method
 * [#27](https://github.com/docker-java/docker-java/pull/27) Added a close method to DockerClient
 * [#26](https://github.com/docker-java/docker-java/pull/26) 
 * [#24](https://github.com/docker-java/docker-java/pull/24) 
 * [#23](https://github.com/docker-java/docker-java/pull/23) 
 * [#22](https://github.com/docker-java/docker-java/pull/22) Fixes for startContainerCmd
 * [#20](https://github.com/docker-java/docker-java/pull/20) 
 * [#19](https://github.com/docker-java/docker-java/pull/19) Add missing options to BuildCmd and set them to match Docker client.
 * [#18](https://github.com/docker-java/docker-java/pull/18) Added Container-Linking
 * [#16](https://github.com/docker-java/docker-java/pull/16) Build image improvements
 * [#15](https://github.com/docker-java/docker-java/pull/15) Collection of changes driven by use in gradle-docker and on Windows
 * [#14](https://github.com/docker-java/docker-java/pull/14) Use RegEx to match ADD command from Dockerfile.
 * [#9](https://github.com/docker-java/docker-java/pull/9) Add a build command accepting a tar as a InputStream, so we can build the Dockerfile TAR on the fly without a temporary folder.
 * [#5](https://github.com/docker-java/docker-java/pull/5) add paused field in ContainerInspectResponse
 * [#6](https://github.com/docker-java/docker-java/pull/6) 
