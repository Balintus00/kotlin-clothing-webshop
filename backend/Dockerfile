FROM gradle:8.4 AS build

COPY --chown=gradle:gradle ./rootProject /home/gradle/src

RUN mkdir /home/gradle/src/backend
COPY --chown=gradle:gradle ./build.gradle.kts /home/gradle/src/backend
COPY --chown=gradle:gradle ./src /home/gradle/src/backend/src

WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:17
EXPOSE 5400:5400
RUN mkdir /app
COPY --from=build /home/gradle/src/backend/build/libs/*.jar /app/backend.jar
ENTRYPOINT ["java","-jar","/app/backend.jar"]