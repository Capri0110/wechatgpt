FROM anapsix/alpine-java:8_server-jre_unlimited

RUN mkdir -p /blade

WORKDIR /blade

EXPOSE 80

ADD ./target/WxMpChat.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]
