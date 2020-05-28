#!/usr/bin/env bash

set -exu

whoami

cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

ssh localhost

exit 1

