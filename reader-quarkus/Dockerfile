FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

COPY samplefile.txt samplefile.txt

COPY target/reader-quarkus-*.jar app-runner.jar

ENTRYPOINT ["java", "-jar", "app-runner.jar"]
