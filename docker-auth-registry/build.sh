#! /bin/sh
set -eux

docker build -t auth-registry .

if [ "$(which boot2docker)" != "" ]; then
	VBoxManage controlvm boot2docker-vm natpf1 "5001,tcp,127.0.0.1,5001,,5001" || true
fi
