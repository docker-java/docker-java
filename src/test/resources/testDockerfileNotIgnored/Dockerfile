FROM      ubuntu:latest

# Copy testrun.sh files into the container

RUN echo "Successful executed testrun.sh" >	/tmp/testrun.sh

RUN cp /tmp/testrun.sh /usr/local/bin/ && chmod +x /usr/local/bin/testrun.sh

CMD ["testrun.sh"]
