#!/usr/bin/env bash


sudo apt-get install -y -q ca-certificates

export HOST_PORT=2375
echo -n | openssl s_client -connect scan.coverity.com:443 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' | sudo tee -a /etc/ssl/certs/ca-certificates.crt


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

./.travis/get-docker-com.sh

sudo -E stop docker

#mkdir "${HOME}/.cache" || :
#pushd "${HOME}/.cache"
# wget -N "https://apt.dockerproject.org/repo/pool/main/d/docker-engine/docker-engine_${DOCKER_VERSION}_amd64.deb"
# sudo apt-get -f install
# sudo dpkg -i "$(ls *${DOCKER_VERSION}*)"
#popd
#rm -f "src/test/resources/logback.xml"
mv "src/test/resources/travis-logback.xml" "src/test/resources/logback.xml"

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
