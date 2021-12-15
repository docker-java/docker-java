Change Log
===


## 3.2.0
- **Changelog is not maintained in this file. Please follow git diff or github releases.**
- Library was split into multiple modules to get ability to choose transports.
Okhttp was added (say thanks to @bsideup).
- Various cleanup, tests de-duplication internally. Planned binary compatibility breakage was reverted by @testcontainers project, so migration should work smoothly. Please switch to non-deprecated methods.
- Appeared various new commands and Fields(command options for existing commands).

## 3.1.2
- update unix-socket to 2.2.0
- Remove `JacksonJaxbJsonProvider` from `FiltersEncoder`
- BuildImageCmdImpl: Fix an exception message
- Add support for target parameter in BuildImgCmd
- Add prune operations
- Updating Jackson due to CVEs
- Make StatsConfig public
- Set 3 mb as limit for json responce. 

## 3.1.1
- Patch save image with tag
- [api/healthcheck] startPeriod is now a long

## 3.1.0 
- Release

## 3.1.0-rc-8
- Do awaitCompletion upon socket close exception
- Fix `X-Registry-Auth` base64 encoding

## 3.1.0-rc-7
- Fix NPE when docker config file doesn't exist

## 3.1.0-rc-6
- Add resize feature to container and exec
- Update part of apis to 1.37
- Update dependencies
- Fix No serializer found for class com.githubdockerjava.api.model.ServiceGlobalModeOptions
- Add HostConfig.StorageOpt and ExecCreateCmd.Env
- Added GCPLOGS enum LoggingType
- Stop proxying HostConfig class (static helper provided)
- Add ExecCreateCmd.Env
- Add failcnt into memorystatsconfig
- Support some missing Engine APIs
- Added memory swappiness to create container command.
- Fix for ignore all files except specified

## 3.1.0-rc-5
- Add missing properties in InspectContainer response
- Add withFilter methods in ListNetworksCmd & ListVolumesCmd
- Add WorkingDir property in containers exec
- Add isolation property support in Info response
- Support platform option in image build/create/pull commands
- Add OSVersion and RootFS support in InspectImageResponse
- Fix double-marshalling of cachefrom
- make AuthConfig compatible with indentitytoken
- Allow netty to handle compressed response bodies

## 3.1.0-rc-4
- Update deps
- fix HTTP/1.1 compliance (missing Host header)
- support rollback_completed value in ServiceUpdateState
- Add Privileged property to ExecCreateCmd
- Encode spaces as %20 rather than + in URL params
- Add tmpfs mount support since v1.29
- Add support for swarm service/task logs
- Add mapping annotations to custom constructor 
- Support network mode as part of the docker build
- support "rollback" as value for UpdateFailureAction
- Add "Pid" field to InspectExecResponse
- Add DiskQuota to HostConfig and CreateContainerCmd
- Add AutoRemove to HostConfig
- follow symbolic links when walking dir
- Use path from the configured docker host instead of hardcoded "/var/run/docker.sock" in netty factory
- Configure JerseyDockerCmdExecFactory to avoid chunked encoding 

## 3.1.0-rc-3
- export TmpFs configuration for HostConfig and DockerClient
- avoid double encoding for url query string

## 3.1.0-rc-2
- https://github.com/docker-java/docker-java/pulls?q=is%3Apr+milestone%3A3.1.0-rc-2+is%3Aclosed

## 3.1.0-rc-1
  A lot of changes...
- Swarm Mode support.
- Classic swarm support.
- various netty improvements
- Implement AbstractDockerCmdExecFactory 
 
 
## 3.0.14
- Encode spaces as %20 rather than + in URL params

## 3.0.13
- Fix .dockerignore handling on Windows
- Include empty directories in build context
 
## 3.0.12
- Make NettyDockerCmdExecFactory has compatibility both Linux and OSX automatically
- Fix double encoding for netty.
- filter config.json before unmarshalling (creds/auth)

## 3.0.11
- Add labels and attachable properties to network.
- Set default socket timeout for RequestConfig.
- Netty skip instead of throw error on non-linux os.
- Clean tmp file after upload.
- Filters ignore application/x-tar.
- Allow user to call connectionManager's closeIdleConnections.
 
## 3.0.10
- Support for cache-from in build image command
- Allow multiple tags in build image command 
- Custom `db` logging type 
- Allow an explicit Dockerfile location string to be specified to theuild command
- Fix image build for docker 17 with 'tagged' word.
 
## 3.0.9
- NettyDockerCmdExecFactory ignores API version 
- exclude commons-logging from httpclient since docker-java uses slf4j/logback
- Generate OSGi compliant manifest
- AuthResponse may contains token.

## 3.0.8
 - Use TLSv1.2 by default
 - Health api
 - Labels
 - Support for multiple certificates 

## 3.0.7
 * https://github.com/docker-java/docker-java/milestone/17?closed=1
 * HostConfig pidLimits
 * Label image during build
 * Expose 'User' property on ExecCreateCmd #707 #708

## 3.0.6
 * Fixed issue with jersey and unix domain sockets.
 * [#703](https://github.com/docker-java/docker-java/pull/703) Allow to configure connection pool timeout.
 * Make all models Serializable.
 * [NETTY] Fix loadImage responce on 1.24 API.
 * LogPath field for inspect container.
 * [#700] (https://github.com/docker-java/docker-java/pull/700) Bugfix:donot throw RuntimeException when a error occured in awaitCompletion(long,TimeUnit)

## 3.0.5
 * Events updated to 1.24 API model.

## 3.0.4
 * Make cert util methods public.

## 3.0.3
 * [JERSEY] Don't send body for start container request.

## 3.0.2
 * Enhanced Dockerignore filtering.
 * Added shmsize for hostconfig.
 * Exposed HostConfig instead of spaghetty calls.

## 3.0.1

All changes
* Updated all dependencies
* [#643] (https://github.com/docker-java/docker-java/pull/643) Fixes for .dockerignore filtering 
* [#627] (https://github.com/docker-java/docker-java/pull/627) Implementation of POST /images/load endpoint 
* [#630] (https://github.com/docker-java/docker-java/pull/630) Fix: Second execution of a docker command in Netty implementation always fails 
* [#596] (https://github.com/docker-java/docker-java/pull/596) Refactor configuration of SSL to allow override with custom config 
* [#529] (https://github.com/docker-java/docker-java/pull/529) Refactor CertUtils. Support ECDSA and PrivateKey 
* [#593] (https://github.com/docker-java/docker-java/pull/593) Added Device.parse() method with simple verification.

v3.0.0
---
Notes

* The 3.0.0 release contains multiple API breaking changes compared to 2.x therefore the major version switch. It supports a subset of v.1.22 of the docker remote API. It also includes an experimental netty based implementation of `DockerCmdExecFactory` that probably will replace the current jersey/httpclient based one in a later release. Take a look at the [Wiki](https://github.com/docker-java/docker-java/wiki#intialize-docker-client-advanced) how to use it.
* The configuration has been changed to better match the docker CLI configuration options. The properties file was renamed from `docker.io.properties` to `docker-java.properties`. See README.md for details.

All changes
* [#590] (https://github.com/docker-java/docker-java/pull/590) Fix default docker.properties to match with docker CLI defaults 
* [#585] (https://github.com/docker-java/docker-java/pull/585) Add RootDir property to GraphData
* [#580] (https://github.com/docker-java/docker-java/pull/580) Fixes execute permissions for files when copied to container 
* [#579] (https://github.com/docker-java/docker-java/pull/579) Adds missing name filter evaluation to netty version of ListImagesCmdExec 
* [#578] (https://github.com/docker-java/docker-java/pull/578) Fix error during image build when Dockerfile in subdirectory of build context
* [#575] (https://github.com/docker-java/docker-java/pull/575) Support binding of port ranges
* [#574] (https://github.com/docker-java/docker-java/pull/574) Fix for copyArchiveToContainerCmd bug
* [#572] (https://github.com/docker-java/docker-java/pull/572) Inspect container command now shows sizes if requested 
* [#563] (https://github.com/docker-java/docker-java/pull/563) Fix memory leak in netty implementation of DockerCmdExecFactory
* [#550] (https://github.com/docker-java/docker-java/pull/550) Add ability to configure IPAM config for CreateNetworkCmd
* [#484] (https://github.com/docker-java/docker-java/pull/484) Implement missing network api options for v1.22

Included in 3.0.0-RC5

* [#542] (https://github.com/docker-java/docker-java/pull/542) Fix large volumes of output from "docker exec" trigger out of memory error
* [#541] (https://github.com/docker-java/docker-java/pull/541) ImageInspectResponse.GraphDriver.Data is more complex structure 
* [#534] (https://github.com/docker-java/docker-java/pull/534) Fix create volume command doesn't assign passed in volume driverOpts to field
* [#533] (https://github.com/docker-java/docker-java/pull/533) Added shmsize build option 

Included in 3.0.0-RC4
* [#528] (https://github.com/docker-java/docker-java/pull/528) Fix DOCKER_TLS_VERIFY cannot be 'false' or empty
* [#527] (https://github.com/docker-java/docker-java/pull/527) Fix `mirrors` field is list and not a single string #527 
* [#510] (https://github.com/docker-java/docker-java/pull/510) Add CgroupParent option for create cmd
* [#509] (https://github.com/docker-java/docker-java/pull/509) Implement container rename api 
* [#501] (https://github.com/docker-java/docker-java/pull/501) Fix execution of copy file/archive command skips 0xFF bytes 
* [#500] (https://github.com/docker-java/docker-java/pull/500) Add aux to ResponseItem
* [#498] (https://github.com/docker-java/docker-java/issues/498) Update image inspect response


Included in 3.0.0-RC3
* [#488] (https://github.com/docker-java/docker-java/pull/488) Support remote API 1.22 subset

Included in 3.0.0-RC2
* [#486] (https://github.com/docker-java/docker-java/pull/486) Fix NegativeArraySizeException in awaitCompletion()
* [#472] (https://github.com/docker-java/docker-java/pull/472) Exec start command: detect end of STDIN stream 
* [#466] (https://github.com/docker-java/docker-java/pull/466) Fix exec start stdin encoding 

Included in 3.0.0-RC1
* [#463] (https://github.com/docker-java/docker-java/pull/463) More logging drivers
* [#447] (https://github.com/docker-java/docker-java/pull/447) Refactoring of DockerClientConfig 
* [#430] (https://github.com/docker-java/docker-java/pull/430) Fix ExecStartCmd failure 
* [#426] (https://github.com/docker-java/docker-java/pull/426) Refactored filters API 
* [#425] (https://github.com/docker-java/docker-java/pull/425) Implement Network API
* [#410] (https://github.com/docker-java/docker-java/pull/410) Support for build-args of docker build 
* [#408] (https://github.com/docker-java/docker-java/pull/408) Support for volume API
* [#406] (https://github.com/docker-java/docker-java/pull/406) Added RestartCount to InspectContainerResponse
* [#396] (https://github.com/docker-java/docker-java/pull/396) Disable evaluation of http.proxy... variables when using unix socket connection 
* [#393] (https://github.com/docker-java/docker-java/pull/393) Support ONBUILD instruction in Dockerfiles 
* [#392] (https://github.com/docker-java/docker-java/pull/392) Introduce InspectContainerResponse.Mounts
* [#387] (https://github.com/docker-java/docker-java/pull/387) Make ProgressDetails attributes public
* [#386] (https://github.com/docker-java/docker-java/pull/386) Basic http proxy configuration support
* [#362] (https://github.com/docker-java/docker-java/pull/362) Deprecate "network" and enable "networks" stats (remote Docker API 1.21) 
* [#359] (https://github.com/docker-java/docker-java/pull/359) Fix performance issue of build command by adding bulk-read variant of InputStream.read()
* [#357] (https://github.com/docker-java/docker-java/pull/357) Wait container command needs possibility to abort operation
* [#347] (https://github.com/docker-java/docker-java/pull/347) Implementation of copy archive to/from container commands 
* [#313] (https://github.com/docker-java/docker-java/pull/313) Refactor primitive type fields to be of object type in JSON objects

v2.2.3
---
* [#487] (https://github.com/docker-java/docker-java/pull/487) Fix NegativeArraySizeException in awaitCompletion() 

v2.2.2
---
* [#478] (https://github.com/docker-java/docker-java/pull/478) Remove debug println

v2.2.1
---
* [#474] (https://github.com/docker-java/docker-java/pull/474) Fix periodic pull failure (2.x)

v2.2.0
---
* [#457] (https://github.com/docker-java/docker-java/pull/457) Input configuration should not be altered as it breaks unix socket support 
* [#430] (https://github.com/docker-java/docker-java/pull/430) Fix ExecStartCmd failure (backported from 3.0.0)

v2.1.4
---

* [#396] (https://github.com/docker-java/docker-java/pull/396) Disable evaluation of http.proxy... variables when using unix socket connection  
* [#359] (https://github.com/docker-java/docker-java/pull/359) Fix performance issue of build command by adding bulk-read variant of InputStream.read()

v2.1.3
---
* [#387] (https://github.com/docker-java/docker-java/pull/387) Make ProgressDetails attributes public
* [#386] (https://github.com/docker-java/docker-java/pull/386) Basic http proxy configuration support
* [#362] (https://github.com/docker-java/docker-java/pull/362) Deprecate "network" and enable "networks" stats (remote Docker API 1.21)

v2.1.2
---
* [#350] (https://github.com/docker-java/docker-java/pull/350) Remove ServiceLoader logic
* [#344] (https://github.com/docker-java/docker-java/pull/344) Implement equals/hashCode for Filters
* [#335] (https://github.com/docker-java/docker-java/pull/335) Improve backward-compatibility support for older API versions
* [#333] (https://github.com/docker-java/docker-java/pull/333) Adding support for withPidMode 

v2.1.1
---
* [#326] (https://github.com/docker-java/docker-java/pull/326) Add all missing fields to ResponseItem and related classes 
* [#320] (https://github.com/docker-java/docker-java/pull/320) Support "since" field for logs command

v2.1.0
---
* [#306] (https://github.com/docker-java/docker-java/pull/306) fix(core): fix NPE if latestItem is null in result callback
* [#305] (https://github.com/docker-java/docker-java/pull/305) chore(core): do not expect default DockerCmdExecFactory service
* [#304] (https://github.com/docker-java/docker-java/pull/304) Throw original exception when command.close() throws Exception too
* [#299] (https://github.com/docker-java/docker-java/pull/299) BuildImage sync to 1.20 API
* [#291] (https://github.com/docker-java/docker-java/pull/291) Moved "Memory", "MemorySwap" and "CpuShares" mappings from ContainerConfig to HostConfig

v2.0.1
---
Release notes
* This is a bugfix release. With this release docker >= v1.7.0 is recommended.

All changes

* [#301] (https://github.com/docker-java/docker-java/pull/301) Respect exception rules in .dockerignore file while build images
* [#298] (https://github.com/docker-java/docker-java/pull/298) Fix repository name validation errors
* [#296] (https://github.com/docker-java/docker-java/pull/296) Fix Build FROM private registry broken with docker 1.7.x 
* [#295] (https://github.com/docker-java/docker-java/pull/295) Support certificate chains in cert.pem
* [#287] (https://github.com/docker-java/docker-java/pull/287) Using the oomKillDisable flag throws a null pointer exception

v2.0.0
---
Release notes

* Some commands APIs has been changed to be callback-driven now to simplify the processing of the result streams for the client application. This affects namely the events, stats, log, attach, build, push and pull commands. Look at the Wiki how to [process events](https://github.com/docker-java/docker-java/wiki#handle-events) or how to [build an image](https://github.com/docker-java/docker-java/wiki#build-image-from-dockerfile) from dockerfile for example.
* The `DockerClientConfig` API has changed to free it from implementation specific configuration options like `readTimeout`, `maxTotalConnections`, `maxPerRouteConnections` and `enableLoggingFilter`. Most options can be configured via `DockerCmdExecFactoryImpl` [programmatically](https://github.com/docker-java/docker-java/wiki#intialize-docker-client-advanced) now. Logging is configurable via [logback](https://github.com/docker-java/docker-java/blob/master/src/test/resources/logback.xml) configuration file in the classpath.

All changes

* [#284](https://github.com/docker-java/docker-java/pull/284) Added GZIP compression for build context creation
* [#282](https://github.com/docker-java/docker-java/pull/282) Remove JAXRS/ApacheConnector implementation specific properties from DockerClientConfig
* [#280](https://github.com/docker-java/docker-java/pull/280) Handle multiple source files in ADD command 
* [#278](https://github.com/docker-java/docker-java/pull/278) Stop leaking tar files in temporary folder
* [#275](https://github.com/docker-java/docker-java/pull/275) Implemented LogConfig (create and inspect containers) 
* [#272](https://github.com/docker-java/docker-java/pull/272) remove withHostConfig() from create container command
* [#270](https://github.com/docker-java/docker-java/pull/270) Passing result callbacks for async commands via commands exec()
* [#269](https://github.com/docker-java/docker-java/pull/269) Add filters option to events operation 
* [#268](https://github.com/docker-java/docker-java/pull/268) Concurrent DockerCmdExecFactory.getDefaultDockerCmdExecFactory fails on reload
* [#263](https://github.com/docker-java/docker-java/pull/263) Refactoring of streaming commands APIs (event, stats, log, attach)
* [#262](https://github.com/docker-java/docker-java/pull/262) Accept filters in list containers 
* [#260](https://github.com/docker-java/docker-java/pull/260) Add labels to create and inspect container

v1.4.0
---
* [#248](https://github.com/docker-java/docker-java/pull/248) Removed deprecated start options
* [#247](https://github.com/docker-java/docker-java/pull/247) Add Domainname attribute on create command
* [#245](https://github.com/docker-java/docker-java/pull/245) Added ReadonlyRootfs option
* [#233](https://github.com/docker-java/docker-java/pull/233) Labels are array of Strings (fixes #232) 
* [#189](https://github.com/docker-java/docker-java/pull/189) Add docker stats support  

v1.3.0
---
* [#213](https://github.com/docker-java/docker-java/pull/213) Add ulimit support
* [#208](https://github.com/docker-java/docker-java/pull/208) Added PullEventStreamItem and EventStreamReader to stream the reading of events
* [#205](https://github.com/docker-java/docker-java/issues/205) Access mode of VolumesRW incorrectly deserialized
* [#204] (https://github.com/docker-java/docker-java/pull/204) Added support to use the credentials from .dockercfg during build
* [#203](https://github.com/docker-java/docker-java/issues/203) Missing 'MacAddress' option in create container command
* [#197](https://github.com/docker-java/docker-java/pull/197) Allow for null bindings

v1.2.0
---
* [#194](https://github.com/docker-java/docker-java/pull/194) Fixed remove intermediate containers bug on build goal
* [#193](https://github.com/docker-java/docker-java/pull/193) Add HostConfig related methods from start command to create command
* [#192](https://github.com/docker-java/docker-java/pull/192) Added a Links constructor accepting a List object

v1.1.0
---
 
 * [#186](https://github.com/docker-java/docker-java/pull/186) Added withPull method to BuilImageCmd 
 * [#185](https://github.com/docker-java/docker-java/pull/185) Introduce WrappedResponseInputStream to close underlying Response
 * [#180](https://github.com/docker-java/docker-java/pull/180) Dockerfiles not called 'dockerfile'
 * [#179](https://github.com/docker-java/docker-java/pull/179) Add support for cpuset in CreateContainerCmd
 * [#170](https://github.com/docker-java/docker-java/pull/170) Allow to specify alternative files other than 'Dockerfile' for building images
 * [#165](https://github.com/docker-java/docker-java/pull/165) PushImageCmd assumes that you have an auth config setup 
 * [#161](https://github.com/docker-java/docker-java/pull/161) Inspect exec command
 * [#159](https://github.com/docker-java/docker-java/pull/159) Add missing Info fields 
 * [#156](https://github.com/docker-java/docker-java/pull/156) Add support for configuring ExtraHosts 
 * [#146](https://github.com/docker-java/docker-java/pull/146) Create Identifier type


v1.0.0
---
 * [#152](https://github.com/docker-java/docker-java/pull/152) Restore guava as a dependency
 * [#149](https://github.com/docker-java/docker-java/pull/149) Handle HTTP-Redirects 
 * [#148](https://github.com/docker-java/docker-java/pull/148) Save image functionality
 * [#142](https://github.com/docker-java/docker-java/pull/142) Reduce Logging Level 
 * [#138](https://github.com/docker-java/docker-java/pull/138) Apache CXF interopabilty
 * [#137](https://github.com/docker-java/docker-java/pull/137) Multiple volumesFrom option when creating a container 
 * [#135](https://github.com/docker-java/docker-java/pull/135) Update to latest unix-socket-factory 
 * [#134](https://github.com/docker-java/docker-java/pull/134) Remove Google Guava as dependency 
 * [#131](https://github.com/docker-java/docker-java/pull/128) Utility classes and streamed JSON representations
 * [#128](https://github.com/docker-java/docker-java/pull/128) Allow unauthorized pullImageCmd

v0.10.5
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

v0.10.4
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

v0.10.3
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

v0.10.2
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

v0.10.1
---

 * [#49](https://github.com/docker-java/docker-java/pull/49) Allow user to check where volume is binded on host
 * [#47](https://github.com/docker-java/docker-java/pull/47) let CompressArchiveUtil preserve executable flags
 * [#46](https://github.com/docker-java/docker-java/pull/46) Fixes to AttachContainerCmd and CreateContainerCmd.

v0.10.0
---

 * [#45](https://github.com/docker-java/docker-java/pull/45) Fix Issue #44 Adjusting DNS property type to be a String array as specified by the Doc...
 * [#38](https://github.com/docker-java/docker-java/pull/38) Remove special-case for one \":\" from PullCommand
 * [#37](https://github.com/docker-java/docker-java/pull/37) Starts v0.10.0
 * [#35](https://github.com/docker-java/docker-java/pull/35) Exposing the withTTY method for container creation.

v0.9.1
---

 * [#31](https://github.com/docker-java/docker-java/pull/31) Change VolumesFrom to handle array
 * [#29](https://github.com/docker-java/docker-java/pull/29) Makes Config a public, immutable class with a builder
 * [#22](https://github.com/docker-java/docker-java/pull/22) Fixes for startContainerCmd
 * [#19](https://github.com/docker-java/docker-java/pull/19) Add missing options to BuildCmd and set them to match Docker client.
 * [#16](https://github.com/docker-java/docker-java/pull/16) Build image improvements

v0.9.0
---

 * [#14](https://github.com/docker-java/docker-java/pull/14) Use RegEx to match ADD command from Dockerfile.
 * [#9](https://github.com/docker-java/docker-java/pull/9) Add a build command accepting a tar as a InputStream, so we can build the Dockerfile TAR on the fly without a temporary folder.

v0.8.2
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

v0.8.1
---


v0.8.1
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
