#!/usr/bin/env bash

SWARM_VERSION="${SWARM_VERSION:-}"
FAST_BUILD="${FAST_BUILD:-}"

## fix coverity issue
sudo apt-get install -y -q ca-certificates
echo -n | openssl s_client -connect scan.coverity.com:443 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' | sudo tee -a /etc/ssl/certs/ca-certificates.crt
##

if [ "$FAST_BUILD" == "true" ]; then
    echo "Fast build, skipping docker installations."
    exit 0
fi

set -exu

sudo ip a ls
sudo ip r ls
sudo ss -antpl

export HOST_PORT="2375"
export SWARM_PORT="2377"
export HOST_IP="$(ip a show dev eth0 | grep "inet\b" | awk '{print $2}' | cut -d/ -f1)"
# because of swarm use docker-engine directly
export PRE_DOCKER_HOST="$DOCKER_HOST"
export DOCKER_HOST="tcp://127.0.0.1:${HOST_PORT}"


docker info
docker version

sudo -E apt-get update
sudo -E apt-get install -q -y wget
sudo -E apt-get -q -y --purge remove docker-engine
sudo -E apt-cache policy docker-engine

./.travis/get-docker-com.sh

sudo -E stop docker

#mkdir "${HOME}/.cache" || :
#pushd "${HOME}/.cache"
# wget -N "https://apt.dockerproject.org/repo/pool/main/d/docker-engine/docker-engine_${DOCKER_VERSION}_amd64.deb"
# sudo apt-get -f install
# sudo dpkg -i "$(ls *${DOCKER_VERSION}*)"
#popd
rm -f "src/test/resources/logback.xml"
#rm -f "src/test/resources/travis-logback.xml"
mv "src/test/resources/travis-logback.xml" "src/test/resources/logback-test.xml"

# https://github.com/docker/docker/issues/18113
sudo rm /var/lib/docker/network/files/local-kv.db

sudo cat /etc/default/docker

cat << EOF | sudo tee /etc/default/docker
DOCKER_OPTS="\
--dns 8.8.8.8 \
--dns 8.8.4.4 \
-D \
-H=unix:///var/run/docker.sock \
-H=tcp://0.0.0.0:${HOST_PORT}  \
--label=com.github.dockerjava.test=docker-java \
"
EOF

sudo cat /etc/default/docker
sudo bash -c ':> /var/log/upstart/docker.log'

date
sudo -E start docker

tries=20
sleep=5
for i in $(seq 1 $tries); do
    if sudo grep "API listen on" /var/log/upstart/docker.log ; then
        echo "Docker started. Delay $(($i * $sleep))"
        break
    elif [[ $i -ge $tries ]]; then
        echo "Docker didn't start. Exiting!"
        sudo cat /var/log/upstart/docker.log
        exit 1
    else
        echo "Docker didn't start, sleeping for 5 secs..."
        sleep $sleep
    fi
done


sudo ss -antpl

curl -V

docker version || sudo cat /var/log/upstart/docker.log
docker info

set +u

cat <<EOF > "${HOME}/.docker-java.properties"
registry.username=${registry_username}
registry.password=${registry_password}
registry.email=${registry_email}
registry.url=https://index.docker.io/v1/

EOF

if [[ -n $SWARM_VERSION ]]; then
#    export SWARM_PORT="${PRE_DOCKER_HOST##*:}"

    docker pull swarm

#    # kv store https://docs.docker.com/v1.11/engine/userguide/networking/get-started-overlay/
#    docker run -d \
#        -p "8500:8500" \
#        -h "consul" \
#        --name=consul \
#        progrium/consul -server -bootstrap
#
#    sleep 5

#    SWARM_TOKEN=$(docker run swarm c)

#    docker run \
#        -d \
#        --name=swarm_manager \
#        -p ${SWARM_PORT}:2375 \
#        "swarm:${SWARM_VERSION}" \
#        manage token://${SWARM_TOKEN}

    docker run \
        -d \
        -p ${SWARM_PORT}:2375 \
        --name=swarm_manager \
        swarm manage --engine-refresh-min-interval "3s" --engine-refresh-max-interval "6s" "nodes://${HOST_IP}:${HOST_PORT}"
#        swarm manage --engine-refresh-min-interval "3s" --engine-refresh-max-interval "6s" "consul://${HOST_IP}:8500"

    # join engine to swarm
    docker run \
        -d \
        "--name=swarm_join" \
        "swarm:${SWARM_VERSION}" \
        join --advertise="${HOST_IP}:${HOST_PORT}" --delay="0s" --heartbeat "5s" "nodes://${HOST_IP}:${HOST_PORT}"
#        join --advertise="${HOST_IP}:${HOST_PORT}" --delay="0s" --heartbeat "5s" "token://${SWARM_TOKEN}"

    docker run --rm \
        "swarm:${SWARM_VERSION}" list "nodes://${HOST_IP}:${HOST_PORT}"

    docker ps -a
    sudo ss -antpl

    sleep 30

    docker logs swarm_join
    docker logs swarm_manager
#    docker logs consul

    # switch to swarm connection
    DOCKER_HOST="$PRE_DOCKER_HOST"

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
