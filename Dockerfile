FROM anapsix/alpine-java

MAINTAINER Jose Leon <leonj1@gmail.com>

RUN apk update && \
    apk add bash bash-doc bash-completion

ADD target/fakeSMTP-2.1-SNAPSHOT.jar /fakeSMTP.jar
ADD bootstrap.sh /

ENTRYPOINT ["/bootstrap.sh"]

