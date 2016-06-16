#!/usr/bin/env bash

env
sudo ss -antpl
sudo ps aufx
set -exu

sudo apt-get update
sudo apt-get install -q -y wget

mkdir "${HOME}/.cache" || :
pushd "${HOME}/.cache"
 wget -N "https://apt.dockerproject.org/repo/pool/main/d/docker-engine/docker-engine_${DOCKER_VERSION}_amd64.deb"
 sudo apt-get -f install
 sudo dpkg -i "$(ls *${DOCKER_VERSION}*)"
popd

echo 'DOCKER_OPTS="-H=unix:///var/run/docker.sock -H=tcp://127.0.0.1:2375"'
sudo restart docker

docker version
docker info
