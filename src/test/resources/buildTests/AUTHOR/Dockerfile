# Nginx
#
# VERSION               0.0.1

FROM      ubuntu:latest
MAINTAINER Guillaume J. Charmes "guillaume@dotcloud.com"

# make sure the package repository is up to date
RUN echo "deb http://archive.ubuntu.com/ubuntu precise main universe" > /etc/apt/sources.list
RUN apt-get update

RUN apt-get install -y nginx