FROM registry

RUN apt-get update
RUN apt-get install -y nginx

ADD server-cert.pem /etc/ssl/certs/docker-registry
ADD server-key.pem /etc/ssl/private/docker-registry

ADD nginx.conf /etc/nginx/
ADD docker-registry.conf /etc/nginx/

ADD docker-registry.htpasswd /etc/nginx/

EXPOSE 5443

ADD start.sh .

CMD ./start.sh
