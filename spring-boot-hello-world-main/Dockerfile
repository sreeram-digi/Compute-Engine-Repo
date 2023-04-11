FROM openjdk:17-jdk-alpine

WORKDIR /spring-boot-hello-world

COPY target/spring-boot-hello-world-0.0.1-SNAPSHOT.jar spring-boot-hello-world.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","spring-boot-hello-world.jar"]
