FROM openjdk:8
WORKDIR /opt
ENV PORT 8087
EXPOSE 8087
COPY target/server.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar