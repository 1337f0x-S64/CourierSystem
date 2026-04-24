FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw 2>/dev/null || true
RUN if [ -f "./mvnw" ]; then ./mvnw clean package -DskipTests; else mvn clean package -DskipTests; fi


FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/couriersystem-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]