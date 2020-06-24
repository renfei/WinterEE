FROM java:8-jre
MAINTAINER RenFei <i@renfei.net>

ADD ./target/WinterEE-Gateway-Serve.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/WinterEE-Gateway-Serve.jar"]

EXPOSE 8081