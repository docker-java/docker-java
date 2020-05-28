#!/usr/bin/env bash

set -exu

sudo apt-get upgrade
sudo apt-get update
sudo apt-get install openssh-server
sudo service ssh start

mkdir -p ~/.ssh
cd ~/.ssh
ssh-keygen -q -t rsa -N "" -f jsch
cat jsch.pub >> authorized_keys
chmod 640 authorized_keys
sudo service ssh restart

cat <<EOT >> config
Host junit-host
	HostName localhost
	StrictHostKeyChecking no
	IdentityFile ~/.ssh/jsch
	PreferredAuthentications publickey
EOT

ssh -q junit-host exit

