# docker-java on circleCI

The build including tests and integration tests can be automatically run on [circleCI](https://circleci.com/).

## Setup
1. create an account on circle CI using your github account.
2. select docker-java from the github projects listed in your profile.
3. go to the project settings for docker-java (click on the gear-wheel icon beside the docker-java title).
4. open the *Environment variable* page.
5. add the following environment variables:
 - DOCKER_EMAIL
 - DOCKER_PASSWORD
 - DOCKER_USERNAME

## Ignored Tests
ExecCreateCmdImplTest.execCreateTest

 - Exec is not supported by the lxc driver

ExecStartCmdImplTest.execStartTest

 - Exec is not supported by the lxc driver

KillContainerCmdImplTest.killContainer

 - Killed container has ExitCode 0

ListImagesCmdImplTest.listDanglingImages

 - caused by [docker#9939](https://github.com/docker/docker/issues/9939)

RemoveContainerCmdImplTest.removeContainer

 - caused by [docker#9939](https://github.com/docker/docker/issues/9939)

RemoveImageCmdImplTest.removeImage

 - caused by [docker#9939](https://github.com/docker/docker/issues/9939)

ContainerDiffCmdImplTest.testContainerDiff

 - too many diffs [{"Kind":0,"Path":"/dev"} ,{"Kind":1,"Path":"/dev/fuse"} ,{"Kind":1,"Path":"/dev/ptmx"} ,{"Kind":1,"Path":"/dev/tty"} ,{"Kind":1,"Path":"/dev/tty1"} ,{"Kind":1,"Path":"/dev/stdout"} ,{"Kind":1,"Path":"/dev/urandom"} ,{"Kind":1,"Path":"/dev/full"} ,{"Kind":1,"Path":"/dev/kmsg"} ,{"Kind":1,"Path":"/dev/null"} ,{"Kind":1,"Path":"/dev/stdin"} ,{"Kind":1,"Path":"/dev/stderr"} ,{"Kind":1,"Path":"/dev/zero"} ,{"Kind":1,"Path":"/dev/fd"} ,{"Kind":1,"Path":"/dev/random"} ,{"Kind":1,"Path":"/test"} ]

BuildImageCmdImplTest.testDockerIgnore

 - ignore is not working

StopContainerCmdImplTest.testStopContainer

 - Stopped container has ExitCode 0

