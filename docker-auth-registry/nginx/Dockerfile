FROM dockerfile/nginx

ADD server-cert.pem /etc/ssl/certs/docker-registry
ADD server-key.pem /etc/ssl/private/docker-registry

ADD nginx.conf /etc/nginx/
ADD docker-registry.conf /etc/nginx/

ADD docker-registry.htpasswd /etc/nginx/

EXPOSE 443

CMD nginx
