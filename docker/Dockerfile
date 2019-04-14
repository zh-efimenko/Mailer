FROM openjdk:11-jre-slim

WORKDIR root/

ADD build/libs/mailer-*.jar ./application.jar

CMD java -server -Xmx256M -jar /root/application.jar