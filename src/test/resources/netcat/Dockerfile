# Firefox over VNC
#
# VERSION               0.3

FROM ubuntu

#install netcat
RUN apt-get install -y netcat

EXPOSE 6900
CMD    ["nc", "-l", "6900"]