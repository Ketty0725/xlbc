FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER ketty

RUN mkdir -p /xlbc/monitor/logs

WORKDIR /xlbc/monitor

EXPOSE 9090

ADD ./target/xlbc-monitor-admin.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
