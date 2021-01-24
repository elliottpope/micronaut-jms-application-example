FROM openjdk:14-alpine

RUN apk add git bash

RUN git clone https://github.com/vishnubob/wait-for-it.git

COPY build/libs/test-application-*-all.jar test-application.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "test-application.jar"]
