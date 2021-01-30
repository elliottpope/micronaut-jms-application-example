FROM gradle:jdk15 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:14-alpine
EXPOSE 8080

RUN apk add git bash

RUN git clone https://github.com/vishnubob/wait-for-it.git

COPY --from=build /home/gradle/src/build/libs/test-application-*-all.jar test-application.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "test-application.jar"]
