FROM maven:3.9.9-amazoncorretto-17-alpine as maven_build

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/

#-Dactive.profile=dev
#RUN mvn package -B -DskipTests
RUN mvn package -B
RUN #mvn clean package -Pnative

#FROM openjdk:8-jre-alpine
#alpine de gcloud vision ile ilgili native bir seyler yok patliyor
#FROM openjdk:8-jre
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=maven_build /build/target/core-0.0.1-SNAPSHOT.jar /app/core.jar

EXPOSE 8008

#ENTRYPOINT ["runsd", "--", "java","-Djava.security.egd=file:/dev/./urandom", "-jar","-Dspring.profiles.active=prod","-Duser.timezone=GMT+3", "core.jar"]
#ENTRYPOINT ["runsd", "--", "java","-Djava.security.egd=file:/dev/./urandom", "-jar","-Duser.timezone=GMT+3", "core.jar"]
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","-Dspring.config.location=file:/config/application.properties","-Duser.timezone=GMT+3", "core.jar"]
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","-Duser.timezone=GMT+3", "core.jar"]
