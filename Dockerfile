
# 1. Java 21 런타임 기반 이미지
FROM eclipse-temurin:21-jdk-alpine

# 2. 작업 디렉토리 지정
WORKDIR /app

# 3. 빌드된 JAR 파일 복사 (Gradle 기준)
COPY build/libs/app.jar app.jar

# 4. 포트 열기 (필요 시)
EXPOSE 8080

# 5. 애플리케이션 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]