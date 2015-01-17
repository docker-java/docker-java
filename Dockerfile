FROM java:7
MAINTAINER https://github.com/docker-java/docker-java

ENV HOME /root
ENV M2_HOME /opt/apache-maven-3.0.5

RUN wget http://www.eu.apache.org/dist/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.tar.gz -O /tmp/apache-maven-3.0.5-bin.tar.gz \
    && cd /tmp \
    && tar xzf apache-maven-3.0.5-bin.tar.gz \
    && mkdir -p /opt \
    && mv apache-maven-3.0.5 /opt \
    && rm apache-maven-3.0.5-bin.tar.gz

WORKDIR /project
ADD . /project/

#ENTRYPOINT $M2_HOME/bin/mvn
CMD $M2_HOME/bin/mvn verify
