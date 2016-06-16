#!/usr/bin/env bash

set -exu

pip install --user codecov

echo "$DIND_IMAGE"
docker pull "$DIND_IMAGE"
docker run --privileged -d -p 4444 -e PORT=4444 --net=host --pid=host "$DIND_IMAGE"
docker version
docker info
DOCKER_HOST="tcp://localhost:4444" DOCKER_TLS_VERIFY=0 docker info
