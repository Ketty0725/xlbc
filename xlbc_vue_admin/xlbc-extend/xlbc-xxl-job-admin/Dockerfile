FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER ketty

RUN mkdir -p /xlbc/xxljob/logs

WORKDIR /xlbc/xxljob

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 9100

ADD ./target/xlbc-xxl-job-admin.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
