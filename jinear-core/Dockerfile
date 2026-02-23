ARG MAVEN_IMAGE=maven:3.9.9-amazoncorretto-17-alpine
ARG RUNTIME_IMAGE=eclipse-temurin:21-jre-alpine
FROM ${MAVEN_IMAGE} AS maven_build

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/

RUN mvn clean package -B

FROM ${RUNTIME_IMAGE}

WORKDIR /app

COPY --from=maven_build /build/target/core-0.0.1-SNAPSHOT.jar /app/core.jar

EXPOSE 8008

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","-Dspring.config.location=file:/config/application.properties","-Duser.timezone=GMT+3", "core.jar"]