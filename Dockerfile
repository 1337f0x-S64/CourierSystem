FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw 2>/dev/null || true
RUN if [ -f "./mvnw" ]; then ./mvnw clean package -DskipTests; else mvn clean package -DskipTests; fi


FROM eclipse-temurin:21-jre

WORKDIR /app

RUN apt-get update && apt-get install -y \
    libx11-6 \
    libxext6 \
    libxi6 \
    libxrender1 \
    libxtst6 \
    libxxf86vm1 \
    libgl1 \
    libgtk-3-0 \
    libasound2 \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/couriersystem-1.0-SNAPSHOT.jar app.jar
COPY --from=builder /app/target/libs libs

ENTRYPOINT ["java", "--enable-preview", "-cp", "app.jar:libs/*", "com.example.courier.Launcher"]