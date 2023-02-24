FROM openjdk:11

MAINTAINER chazunik@gmail.com

ENV TZ=Asia/Seoul

EXPOSE 8080

COPY ./build/libs/ohimarket-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]