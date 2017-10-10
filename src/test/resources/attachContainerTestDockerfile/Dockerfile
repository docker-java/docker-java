FROM busybox:latest

ADD	./echo.sh /tmp/

RUN mkdir -p /usr/local/bin
RUN cp /tmp/echo.sh /usr/local/bin/ && chmod +x /usr/local/bin/echo.sh

CMD ["echo.sh"]

