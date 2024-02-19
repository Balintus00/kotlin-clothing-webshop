FROM gradle:8.4 AS build

COPY --chown=gradle:gradle ./rootProject /home/gradle/src
COPY --chown=gradle:gradle ./build.gradle.kts /home/gradle/src
RUN mkdir /home/gradle/src/src
COPY --chown=gradle:gradle ./src /home/gradle/src/src

WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:17
EXPOSE 5400:5400
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/backend.jar
ENTRYPOINT ["java","-jar","/app/backend.jar"]