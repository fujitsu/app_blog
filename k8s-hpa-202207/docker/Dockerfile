FROM eclipse-temurin:17.0.3_7-jdk-alpine

RUN mkdir /app
COPY hpa-uber.jar /app
CMD java -Xmx856m -Xms856m -jar /app/hpa-uber.jar
