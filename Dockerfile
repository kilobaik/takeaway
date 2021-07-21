FROM mcr.microsoft.com/java/jdk:8-zulu-ubuntu

RUN apt-get update && apt-get --yes --no-install-recommends install curl libcap2-bin
COPY target/*.jar app.jar

EXPOSE 8080

CMD java -jar app.jar
