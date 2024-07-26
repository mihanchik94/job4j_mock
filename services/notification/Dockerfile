# сборка проекта в jar
FROM maven:3.6.3-openjdk-17 as maven
WORKDIR /notification
COPY . /notification
RUN mvn install

# указание, как запустить проект
FROM openjdk:17.0.2-jdk
WORKDIR /notification
COPY --from=maven /notification/target/notification-1.0.0.jar notification.jar
CMD java -jar notification.jar