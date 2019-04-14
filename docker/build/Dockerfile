# Build
FROM openjdk:11-jdk-slim as build
WORKDIR /root/application
COPY . .
RUN ./gradlew clean build

# Image
FROM openjdk:11-jre-slim
WORKDIR /root
COPY --from=build /root/application/build/libs/mailer*.jar /root/

CMD java -server -Xmx256M -jar /root/mailer*.jar