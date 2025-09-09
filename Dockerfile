FROM openjdk:21-jdk

COPY /target/product-service-0.0.1-SNAPSHOT.jar app/product-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","app/product-service-0.0.1-SNAPSHOT.jar"]