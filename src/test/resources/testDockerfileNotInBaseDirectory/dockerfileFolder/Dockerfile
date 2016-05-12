FROM ubuntu:latest

ADD	testrunFolder/testrun.sh       /tmp/

RUN cp /tmp/testrun.sh /usr/local/bin/ && chmod +x /usr/local/bin/testrun.sh

CMD ["testrun.sh"]