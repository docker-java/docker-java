#!/usr/bin/env bash

set -exu

SWARM_VERSION="${SWARM_VERSION:-}"
DOCKER_VERSION="${DOCKER_VERSION:-}"
DOCKER_HOST="${DOCKER_HOST:-}"

export HOST_PORT="2375"

rm -f "docker-java/src/test/resources/logback.xml"
mv "docker-java/src/test/resources/travis-logback.xml" "docker-java/src/test/resources/logback-test.xml"

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
ExecStart=/usr/bin/dockerd -H unix:///var/run/docker.sock -H tcp://0.0.0.0:${HOST_PORT}
    " | sudo tee -a /etc/systemd/system/docker.service.d/override.conf

    sudo systemctl daemon-reload
    sudo service docker restart || sudo journalctl -xe
    sudo service docker status
fi

while (! docker ps ); do
  # Docker takes a few seconds to initialize
  echo "Waiting for Docker to launch..."
  sleep 1
done

docker version
docker info

set +u

cat <<EOF > "${HOME}/.docker-java.properties"
registry.username=${registry_username}
registry.password=${registry_password}
registry.email=${registry_email}
registry.url=https://index.docker.io/v1/

EOF

if [[ -n $SWARM_VERSION ]]; then
    export SWARM_PORT="2377"
    export HOST_IP="$(ip a show dev eth0 | grep "inet\b" | awk '{print $2}' | cut -d/ -f1)"

    docker pull swarm

    docker run \
        -d \
        -p ${SWARM_PORT}:2375 \
        --name=swarm_manager \
        "swarm:${SWARM_VERSION}" \
        manage --engine-refresh-min-interval "3s" --engine-refresh-max-interval "6s" "nodes://${HOST_IP}:${HOST_PORT}"

    # join engine to swarm
    docker run \
        -d \
        "--name=swarm_join" \
        "swarm:${SWARM_VERSION}" \
        join --advertise="${HOST_IP}:${HOST_PORT}" --delay="0s" --heartbeat "5s" "nodes://${HOST_IP}:${HOST_PORT}"

    docker run --rm \
        "swarm:${SWARM_VERSION}" \
        list "nodes://${HOST_IP}:${HOST_PORT}"

    docker ps -a

    sleep 30

    docker logs swarm_join
    docker logs swarm_manager

    # switch to swarm connection
    export DOCKER_HOST="tcp://127.0.0.1:${SWARM_PORT}"

    docker version
    docker info

    NODES=$(docker info | grep "Nodes:" | awk '{ print $2 }')
    if [[ $NODES -eq "0" ]]; then
        echo "Swarm didn't connect"
        exit 1
    fi

    # test via swarm
    docker pull busybox
fi
