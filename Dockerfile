# jar는 GitHub Actions 러너에서 ./gradlew build로 미리 만들고, 이미지는 JRE에 jar만 복사한다.
# (t3.micro에서 Gradle 빌드를 하지 않기 위함)
FROM eclipse-temurin:21-jre

WORKDIR /app

ENV TZ=Asia/Seoul

COPY build/libs/*.jar app.jar

EXPOSE 8080

# JAVA_OPTS는 docker-compose에서 주입 (메모리 1GB 인스턴스에 맞춘 힙 크기 등)
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
