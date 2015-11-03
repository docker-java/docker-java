FROM      ubuntu:latest

ENV varA ./subFolder
ENV varB $varA/testrun.sh 

ADD	$varB       /tmp/

RUN cp /tmp/testrun.sh /usr/local/bin/ && chmod +x /usr/local/bin/testrun.sh

CMD ["testrun.sh"]
