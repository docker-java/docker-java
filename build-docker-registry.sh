#! /bin/sh
set -eux

git clone https://github.com/docker/docker-registry
cp docker-registry/contrib/nginx/nginx_1–3–9.conf /etc/nginx/conf.d/