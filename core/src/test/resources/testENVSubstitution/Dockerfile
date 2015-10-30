FROM      ubuntu:latest

# Copy testrun.sh files into the container

ENV variable abc123
ADD ./testrun.sh /tmp/
ADD	./subst-file-$variable.txt     /tmp/
COPY ./subst-file-2-${variable}.txt     /tmp/
RUN cp /tmp/testrun.sh /usr/local/bin/ && chmod +x /usr/local/bin/testrun.sh

CMD ["testrun.sh"]
