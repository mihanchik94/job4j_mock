# сборка проекта в jar
FROM maven:3.6.3-openjdk-17 as maven
WORKDIR /mock
COPY . /mock
RUN mvn install

# указание, как запустить проект
FROM openjdk:17.0.2-jdk
WORKDIR /mock
COPY --from=maven /mock/target/mock-1.0.0.jar mock.jar
CMD java -jar mock.jar