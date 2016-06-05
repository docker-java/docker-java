FROM      ubuntu:latest

# Copy testrun.sh files into the container

ADD	http://www.example.com/index.html       /tmp/some.html
ADD	./testrun.sh       /tmp/

RUN cp /tmp/testrun.sh /usr/local/bin/ && chmod +x /usr/local/bin/testrun.sh

CMD ["testrun.sh"]