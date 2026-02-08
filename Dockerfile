FROM eclipse-temurin:21-jdk
ADD target/sevana.jar sevana.jar
ENTRYPOINT ["java","-jar","/sevana.jar"]

