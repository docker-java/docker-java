#!/usr/bin/env bash


if [ "$FAST_BUILD" == true ]; then
    echo "Fast build, skipping docker installations."
    exit 0
fi

set -exu

docker info
docker version

sudo -E apt-get update
sudo -E apt-get install -q -y wget
sudo -E apt-get -q -y --purge remove docker-engine
sudo -E apt-cache policy docker-engine

./get-docker-com.sh
#mkdir "${HOME}/.cache" || :
#pushd "${HOME}/.cache"
# wget -N "https://apt.dockerproject.org/repo/pool/main/d/docker-engine/docker-engine_${DOCKER_VERSION}_amd64.deb"
# sudo apt-get -f install
# sudo dpkg -i "$(ls *${DOCKER_VERSION}*)"
#popd
#rm -f "src/test/resources/logback.xml"
mv "src/test/resources/travis-logback.xml" "src/test/resources/logback.xml"

echo 'DOCKER_OPTS="-H=unix:///var/run/docker.sock -H=tcp://127.0.0.1:2375"' | sudo tee -a /etc/default/docker
sudo -E restart docker
sleep 10
docker version
docker info

set +u

cat <<EOF > "${HOME}/.docker-java.properties"
registry.username=${registry_username}
registry.password=${registry_password}
registry.email=${registry_email}
registry.url=https://index.docker.io/v1/

EOF
