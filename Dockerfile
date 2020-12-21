From openjdk:8
copy ./target/saha-0.0.1-SNAPSHOT.jar saha.jar
CMD ["java","-jar","saha.jar"]
