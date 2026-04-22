# ---- Build stage ----
    FROM maven:3.9.8-eclipse-temurin-17 AS build
    WORKDIR /app
    
    # Cache dependencies first
    COPY pom.xml .
    RUN mvn -q -DskipTests dependency:go-offline
    
    # Build application
    COPY src ./src
    RUN mvn -q -DskipTests clean package
    
    # ---- Runtime stage ----
    FROM eclipse-temurin:17-jre
    WORKDIR /app
    
    # Security: run as non-root user
    RUN useradd -ms /bin/bash spring
    USER spring
    
    COPY --from=build /app/target/*.jar app.jar
    
    # Render provides PORT dynamically
    ENV PORT=8080
    EXPOSE 8080
    
    ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]