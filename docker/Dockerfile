FROM openjdk:17-jdk-alpine
# Imposta la directory di lavoro
WORKDIR /app

# Copia il file JAR dell'applicazione nella directory di lavoro
COPY target/*.jar app.jar

# Espone la porta su cui l'app Spring Boot è in ascolto
EXPOSE 8080

# Comando per eseguire l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]