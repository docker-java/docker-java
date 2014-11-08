#! /bin/sh
set -eu


echo "enter "registry" each time you are asked for a pass-phase"
echo "enter "nginx" when requested for a common name"
echo "press enter for everything else"

htpasswd -bc docker-registry.htpasswd registry registry

echo 01 > ca.srl
openssl genrsa -des3 -out ca-key.pem 2048
openssl req -new -x509 -days 365 -key ca-key.pem -out ca.pem

openssl genrsa -des3 -out server-key.pem 2048
openssl req -subj "/CN=registry" -new -key server-key.pem -out server.csr
openssl x509 -req -days 365 -in server.csr -CA ca.pem -CAkey ca-key.pem -out server-cert.pem

openssl rsa -in server-key.pem -out server-key.pem
