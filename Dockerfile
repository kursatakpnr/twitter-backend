# --- Stage 1: Build ---
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Maven wrapper ve pom.xml kopyala (bağımlılık cache için)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Maven wrapper'ı çalıştırılabilir yap ve bağımlılıkları indir
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Kaynak kodu kopyala ve jar oluştur
COPY src src
RUN ./mvnw package -DskipTests -B

# --- Stage 2: Run ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Güvenlik için root olmayan kullanıcı
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=build /app/target/*.jar app.jar

RUN chown appuser:appgroup app.jar
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
