# сборка проекта в jar
FROM maven:3.6.3-openjdk-17 as maven
WORKDIR /desk
COPY . /desk
RUN mvn install

# указание, как запустить проект
FROM openjdk:17.0.2-jdk
WORKDIR /desk
COPY --from=maven /desk/target/desc-1.0.0.jar desc.jar
CMD java -jar desk.jar