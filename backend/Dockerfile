FROM maven:3.9.1 AS build
RUN mkdir -p /backend
WORKDIR /backend
COPY pom.xml /backend
COPY  src /backend/src
RUN mvn -f pom.xml clean
RUN mvn install -DskipTests

FROM openjdk:17-oracle
COPY --from=build /backend/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080