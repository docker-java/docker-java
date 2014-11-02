# https://medium.com/@deeeet/building-private-docker-registry-with-basic-authentication-with-self-signed-certificate-using-it-e6329085e612

FROM registry

RUN apt-get update
RUN apt-get install -y nginx

ADD nginx.conf /etc/nginx/
ADD docker-registry.conf /etc/nginx/

ADD docker-registry.htpasswd /etc/nginx/

EXPOSE 5001

ADD start.sh .

CMD ./start.sh
