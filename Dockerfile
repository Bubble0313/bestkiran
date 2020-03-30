FROM java:8-jdk-alpine
COPY ./target/practice-0.0.1-SNAPSHOT.jar /trial/
WORKDIR /trial/
ENTRYPOINT ["java","-jar","practice-0.0.1-SNAPSHOT.jar"]