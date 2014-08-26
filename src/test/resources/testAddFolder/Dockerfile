FROM      ubuntu

# Copy testrun.sh files into the container

ADD .   /src/

run ls -la /src

run cp /src/folderA/testAddFolder.sh /usr/local/bin/

CMD ["testAddFolder.sh"]
