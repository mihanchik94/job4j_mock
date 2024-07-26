# сборка проекта в jar
FROM maven:3.6.3-openjdk-17 as maven
WORKDIR /auth
COPY . /auth
RUN mvn install

# указание, как запустить проект
FROM openjdk:17.0.2-jdk
WORKDIR /auth
COPY --from=maven /auth/target/auth-1.0.0.jar auth.jar
CMD java -jar auth.jar