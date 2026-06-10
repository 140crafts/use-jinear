ARG MAVEN_IMAGE=maven:3.9.9-amazoncorretto-17-alpine
ARG RUNTIME_IMAGE=eclipse-temurin:21-jre-alpine
FROM ${MAVEN_IMAGE} AS maven_build

WORKDIR /build/

# Resolve dependencies first so this layer is cached unless pom.xml changes
COPY pom.xml /build/
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B

# Build sources; only re-runs when src changes
COPY src /build/src/
RUN --mount=type=cache,target=/root/.m2 mvn clean package -B

FROM ${RUNTIME_IMAGE}

WORKDIR /app

COPY --from=maven_build /build/target/core-0.0.1-SNAPSHOT.jar /app/core.jar

EXPOSE 8008

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","-Dspring.config.location=file:/config/application.properties","-Duser.timezone=GMT+3", "core.jar"]