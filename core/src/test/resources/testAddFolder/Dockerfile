FROM      ubuntu:latest

# Copy testrun.sh files into the container

ADD .   /src/

run ls -la /src

run cp /src/folderA/testAddFolder.sh /usr/local/bin/ && chmod +x /usr/local/bin/testAddFolder.sh

CMD ["testAddFolder.sh"]
