#! /bin/sh
set -eux

if [ ! -e server-key.pem ]; then
    echo "enter dockerjava each time you are asked for a pass-phase, press enter for everything else"

    echo 01 > ca.srl
    openssl genrsa -des3 -out ca-key.pem 2048
    openssl req -new -x509 -days 365 -key ca-key.pem -out ca.pem
    openssl genrsa -des3 -out server-key.pem 2048
    openssl req -subj '/CN=localhost' -new -key server-key.pem -out server.csr
    openssl x509 -req -days 365 -in server.csr -CA ca.pem -CAkey ca-key.pem -out server-cert.pem
    openssl rsa -in server-key.pem -out server-key.pem
fi

docker build -t auth-registry .

if [ "$(which boot2docker)" != "" ]; then
    B=$(echo $DOCKER_HOST|sed 's/.*\/\(.*\):.*/\1/')

    scp -i ~/.ssh/id_boot2docker ca.pem docker@$B:

    echo "sudo su -
chmod +w /etc/ssl/certs/ca-certificates.crt
cat ca.pem >> /etc/ssl/certs/ca-certificates.crt" | boot2docker ssh

	VBoxManage controlvm boot2docker-vm natpf1 "5001,tcp,127.0.0.1,5001,,5001" || true
fi
