FROM      busybox:latest

# Copy testrun.sh files into the container

ADD .   /src/

RUN ls -la /src

RUN mkdir -p /usr/local/bin
RUN cp /src/folderA/testAddFolder.sh /usr/local/bin/ && chmod +x /usr/local/bin/testAddFolder.sh

CMD ["testAddFolder.sh"]
