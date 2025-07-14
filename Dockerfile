FROM openjdk:17-jdk-alpine

COPY deck-center-*.jar /opt/deck-center/deck-center.jar

WORKDIR /opt/deck-center

VOLUME /opt/deck-center/config

EXPOSE 38383/tcp
EXPOSE 38443/tcp

ENV JVM_XMX 512M
ENV JVM_XMS 4096M

CMD java -server -Xmx$JVM_XMX -Xms$JVM_XMS -jar deck-center.jar


