FROM  eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/bistral-config-server.jar bistral-api-gateway-service.jar

EXPOSE 8085

ENTRYPOINT ["java","-jar","bistral-api-gateway-service.jar"]
