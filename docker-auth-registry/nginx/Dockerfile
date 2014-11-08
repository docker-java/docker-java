FROM dockerfile/nginx

ADD server-cert.pem /etc/ssl/certs/nginx
ADD server-key.pem /etc/ssl/private/nginx

ADD registry touch /etc/nginx/sites-available/registry
RUN ln -s ln -s /etc/nginx/sites-available/registry /etc/nginx/sites-enabled

ADD nginx.htpasswd /etc/nginx/

EXPOSE 443

CMD nginx
