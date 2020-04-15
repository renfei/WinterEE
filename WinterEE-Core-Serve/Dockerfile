FROM java:8-jre
MAINTAINER RenFei <i@renfei.net>

ADD ./target/WinterEE-Core-Serve.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/WinterEE-Core-Serve.jar"]

EXPOSE 8090