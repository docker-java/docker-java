FROM      busybox:latest

RUN mkdir -p /usr/local/bin
# Copy testrun.sh files into the container

ONBUILD ADD ./testrun.sh    /tmp/

ONBUILD RUN cp /tmp/testrun.sh /usr/local/bin/ && chmod +x /usr/local/bin/testrun.sh

CMD ["testrun.sh"]
