FROM      ubuntu

# Copy testrun.sh files into the container

add	. /src/

run ls -la /src

run cp /src/folderA/testAddFolder.sh /usr/local/bin/ && chmod +x /usr/local/bin/testAddFolder.sh

CMD ["testAddFolder.sh"]
