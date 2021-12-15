FROM busybox:latest

ADD	testrunFolder/testrun.sh       /tmp/

RUN mkdir -p /usr/local/bin
RUN cp /tmp/testrun.sh /usr/local/bin/ && chmod +x /usr/local/bin/testrun.sh

CMD ["testrun.sh"]