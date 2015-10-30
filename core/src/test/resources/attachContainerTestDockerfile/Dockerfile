FROM ubuntu:latest

ADD	./echo.sh /tmp/

RUN cp /tmp/echo.sh /usr/local/bin/ && chmod +x /usr/local/bin/echo.sh

CMD ["echo.sh"]

