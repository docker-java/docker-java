#! /bin/sh
set -eu

function build() {
    echo "building..."
    if [ ! -e done ]; then
        echo "enter dockerjava each time you are asked for a pass-phase"
        H=$(hostname)
        echo "enter $H when requested for a common name"
        echo "press enter for everything else"

        echo 01 > ca.srl
        openssl genrsa -des3 -out ca-key.pem 2048
        openssl req -new -x509 -days 365 -key ca-key.pem -out ca.pem

        openssl genrsa -des3 -out server-key.pem 2048
        openssl req -subj "/CN=$H" -new -key server-key.pem -out server.csr
        openssl x509 -req -days 365 -in server.csr -CA ca.pem -CAkey ca-key.pem -out server-cert.pem

        openssl rsa -in server-key.pem -out server-key.pem

        if [ "$(which boot2docker)" != "" ]; then
            B=$(echo $DOCKER_HOST|sed 's/.*\/\(.*\):.*/\1/')

            scp -i ~/.ssh/id_boot2docker ca.pem docker@$B:

            echo "mkdir /etc/docker/certs.d/localhost:5443/" | boot2docker ssh
            echo "cat ca.pem > /etc/docker/certs.d/localhost:5443/ca.crt" | boot2docker ssh
        fi

        touch done
    fi

    docker build -t auth-registry .

    if [ "$(which boot2docker)" != "" ]; then
        VBoxManage controlvm boot2docker-vm natpf1 "5443,tcp,127.0.0.1,5443,,5443" || true
    fi
}

function start() {
    echo "starting..."
    docker run -P -p 5443:5443 -d auth-registry
    sleep 2s
}

function stop() {
    PS=$(docker ps|grep auth-registry|awk '{print $1}')
    if [ "" != "$PS" ]; then
        echo "stopping..."
        docker kill $PS
    fi
    if [ "$(which boot2docker)" != "" ]; then
        VBoxManage controlvm boot2docker-vm natpf1 delete 5443 || true
    fi
}

function testIt() {
    echo "testing..."
    curl https://localhost:5443/v1/_ping -f -k
    echo
    curl https://localhost:5443/v1/users/ -k -f --basic --user dockerjava:dockerjava
    echo
}

C=${1:-''}

cd container

case $C in
    build) build ;;
    start) start ;;
    stop) stop ;;
    test) testIt ;;
    '')
        stop
        build
        start
        testIt
        ;;
    *)
        echo "$(basename $0) (build|start|stop|test)"
esac

