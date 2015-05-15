FROM busybox:latest

# log to stdout and stderr so we can make sure logging with FrameReader works

RUN echo '#! /bin/sh' > cmd.sh
RUN echo 'echo "to stdout"' >> cmd.sh
RUN echo 'echo "to stderr" > /dev/stderr' >> cmd.sh
RUN echo 'sleep 1' >> cmd.sh
RUN chmod +x cmd.sh

CMD ["./cmd.sh"]