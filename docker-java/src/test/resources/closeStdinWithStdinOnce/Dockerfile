FROM busybox:latest

ADD	echo_stdin.sh /tmp/

RUN mkdir -p /usr/local/bin
RUN cp /tmp/echo_stdin.sh /usr/local/bin/ && chmod +x /usr/local/bin/echo_stdin.sh

CMD ["echo_stdin.sh"]

