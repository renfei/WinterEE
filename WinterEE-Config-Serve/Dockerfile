FROM java:8-jre
MAINTAINER RenFei <i@renfei.net>

ADD ./target/WinterEE-Config-Serve.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/WinterEE-Config-Serve.jar"]

EXPOSE 8114