#!/usr/bin/env bash

set -exu

DOCKER_VERSION="${DOCKER_VERSION:-}"
DOCKER_HOST="${DOCKER_HOST:-}"

if [[ -n $DOCKER_VERSION ]]; then
    sudo -E apt-get -q -y --purge remove docker-engine docker-ce

    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
    sudo apt-get update
    sudo apt-cache madison docker-ce
    sudo apt-get install "docker-ce=$DOCKER_VERSION"
fi

if [[ -n $DOCKER_HOST ]]; then
    sudo mkdir -p /etc/systemd/system/docker.service.d/

    echo "
[Service]
ExecStart=
ExecStart=/usr/bin/dockerd -H $DOCKER_HOST
    " | sudo tee -a /etc/systemd/system/docker.service.d/override.conf

    sudo systemctl daemon-reload
    sudo service docker restart || sudo journalctl -xe
    sudo service docker status
fi

while (! docker ps ); do
  echo "Waiting for Docker to launch..."
  sleep 1
done
docker version
docker info

docker run --rm hello-world
