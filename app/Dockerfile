FROM eclipse-temurin:21-jdk AS build

WORKDIR /workspace/app
COPY . /workspace/app

RUN ./mvnw -B -DskipTests=true package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
