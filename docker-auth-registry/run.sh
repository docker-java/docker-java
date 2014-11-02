#! /bin/sh
set -eux

docker kill $(docker ps -q) || true

docker run -p 5001:5001 auth-registry