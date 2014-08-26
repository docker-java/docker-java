FROM      ubuntu:latest

# Copy testrun.sh files into the container

ADD	./testrun.sh       /tmp/
ADD ./oldFile.txt 	   /	
RUN cp /tmp/testrun.sh /usr/local/bin/

CMD ["testrun.sh"]
