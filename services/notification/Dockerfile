FROM maven:3.6.3-openjdk-17

RUN mkdir auth

WORKDIR auth

COPY . .

RUN mvn package -Dmaven.test.skip=true

CMD ["mvn", "liquibase:update", "-Pdocker"]

CMD ["java", "-jar", "target/notification-1.0.0.jar"]