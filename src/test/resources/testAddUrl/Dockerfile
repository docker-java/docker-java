FROM      ubuntu:latest

# Copy testrun.sh files into the container

ADD	http://www.docker.io       /tmp/docker_home.html
ADD	./testrun.sh       /tmp/

RUN cp /tmp/testrun.sh /usr/local/bin/

CMD ["testrun.sh"]