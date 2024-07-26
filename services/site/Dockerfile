# сборка проекта в jar
FROM maven:3.6.3-openjdk-17 as maven
WORKDIR /site
COPY . /site
RUN mvn install

# указание, как запустить проект
FROM openjdk:17.0.2-jdk
WORKDIR /site
COPY --from=maven /site/target/site-1.0.0.jar site.jar
CMD java -jar site.jar