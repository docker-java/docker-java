FROM      busybox:latest

# Copy testrun.sh files into the container

ADD ./folder*/*    /tmp/

RUN mkdir -p /usr/local/bin
RUN cp /tmp/*.sh /usr/local/bin/ && chmod +x /usr/local/bin/*.sh

CMD ["testrun.sh"]
