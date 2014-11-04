#! /bin/sh
set -eux

docker-registry &
nginx

wait

