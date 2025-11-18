# 1. Build Stage
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

# Gradle 설정 복사 및 캐싱
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

# 소스 복사 및 빌드 (테스트 제외)
COPY src src
RUN ./gradlew clean build -x test --no-daemon

# 2. Run Stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]