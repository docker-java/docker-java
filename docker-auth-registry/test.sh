#! /bin/sh
set -eux

curl http://localhost:5001/v1/_ping 
curl http://localhost:5001/v1/users/ --basic --user dockerjava:dockerjava

