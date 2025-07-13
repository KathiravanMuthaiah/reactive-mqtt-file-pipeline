# Reactive File Processor - Dual Microservice (Spring Boot & Quarkus)

This project demonstrates reactive file processing using both **Spring Boot (JDK 17)** and **Quarkus**, with the same domain logic implemented in two separate microservices. It is designed to:

- Read large text files line-by-line in a non-blocking way
- Transform the line by appending the sum of comma-separated values
- Store original data in PostgreSQL using JPA
- Publish transformed lines to MQTT topic
- Write a summary record via raw JDBC after file processing
- Write the output (from MQTT) to another file reactively

---

## 📁 Project Structure

```text
project-root/
├── reader-springboot/        # Spring Boot microservice for reading & publishing
├── writer-springboot/        # Spring Boot microservice for subscribing & writing
├── reader-quarkus/           # Quarkus-based equivalent of reader-springboot
├── writer-quarkus/           # Quarkus-based equivalent of writer-springboot
├── .vscode/                  # VS Code settings for project development


```

## 🚀 Technologies Used

| Component        | Technology                      |
| ---------------- | ------------------------------- |
| Language         | Java 17                         |
| Build Tool       | Maven                           |
| Frameworks       | Spring Boot, Quarkus            |
| Reactive Lib     | Project Reactor (Spring)        |
| Messaging        | MQTT (Mosquitto)                |
| Database         | PostgreSQL 15                   |
| JPA              | Hibernate (Spring) / Panache    |
| JDBC Summary     | Spring JDBC Template / Java SQL |
| Containerization | Docker                          |



------

## 🛠 External Dependencies

These are run separately via Docker Compose (outside the project):

- PostgreSQL (15)
- Mosquitto MQTT Broker

Your `.env` and docker-compose files should already define these.

------

## 🔧 Configuration

Each microservice connects to external services via:

- `application.yml` or `application.properties`
- Uses Spring/Quarkus environment loading
- `.env` files can be used in Docker Compose context

------

## 🧪 Running Locally (Spring Boot Reader Example)

```bash
# Navigate to reader-springboot
cd reader-springboot

# Build
mvn clean package

# Run (with input file path)
java -jar target/reader-springboot.jar --file=input/data.txt
```

Or via Docker:

```bash
docker-compose -f ../docker-compose.reader-springboot.yaml up
```

License: MIT
