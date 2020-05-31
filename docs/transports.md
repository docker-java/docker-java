# Available transports

## Apache HttpClient 5
| | |
|---|---|
| Maven coordinates | `com.github.docker-java:docker-java-transport-httpclient5` |
| Stabilitty | 🙂|
| Long term support plans | ✅ |
| Unix sockets support | ✅ |
| Windows Npipe support | ✅ |
| Stdin attachment support | ✅ |

This transport is based on Apache HttpClient library version 5, which has a great flexibility and allows us to implement all Docker-specific features and protocols required, without having to use internal APIs or anything.

It has everything to become the default transport of docker-java in future releases.

## "Zerodep"
| | |
|---|---|
| Maven coordinates | `com.github.docker-java:docker-java-transport-zerodep` |
| Stabilitty | 🙂|
| Long term support plans | ✅ |
| Unix sockets support | ✅ |
| Windows Npipe support | ✅ |
| Stdin attachment support | ✅ |

The idea of this transport is to provide a transport that supports 100% of the features without having to sorry about transitive dependencies.

Note: due to the implementation details, it cannot be true "0 dependencies" module, so it needs to depend on `slf4j-api` and JNA.

## OkHttp
| | |
|---|---|
| Maven coordinates | `com.github.docker-java:docker-java-transport-okhttp` |
| Stabilitty | 🧐|
| Long term support plans | ❓ |
| Unix sockets support | ✅ |
| Windows Npipe support | ✅ |
| Stdin attachment support | ✅ |

The OkHttp transport was first implemented in [the Testcontainers library](http://github.com/testcontainers/testcontainers-java) as a replacement for Netty. The main motivation for it was to not have heavy-weight Netty-specific native dependencies and the lack of Npipe support in the Netty one.

OkHttp's migration to Kotlin and the need to use internal APIs for doing stdin hijacking makes us question the future of this transport (still under the consideration).

## Netty
| | |
|---|---|
| Maven coordinates | `com.github.docker-java:docker-java-transport-netty` |
| Stabilitty | 🧐|
| Long term support plans | ❌ |
| Unix sockets support | ✅ |
| Windows Npipe support | ❌ |
| Stdin attachment support | ✅ |

Netty was the first alternative transport introduced as an alternative to Jersey.

Although it gives a very low level access to the protocol, the lack of Windows Npipe support and the native library dependency for Unix Sockets make it hard to maintain and there are no plans to continue including this transport option in future versions.

The community may decide to pick it up and continue the development as a 3rd party transport based on the existing abstractions `docker-java` provides.

## Jersey
| | |
|---|---|
| Maven coordinates | `com.github.docker-java:docker-java-transport-jersey` |
| Stabilitty | 🙃|
| Long term support plans | ❌ |
| Unix sockets support | ✅ |
| Windows Npipe support | ❌ |
| Stdin attachment support | ❌ |

Jersey was the initial transport of the project. And, while working well, it was lacking support for connection hijacking (e.g. stdin attachment) or Windows Npipes.
The big amount of dependencies was also causing issues.

Since Apache HttpClient 5-based transport is available now, there is no reason to keep Jersey and it will eventually be removed.