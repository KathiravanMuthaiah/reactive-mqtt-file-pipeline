FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY samplefile.txt samplefile.txt
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
