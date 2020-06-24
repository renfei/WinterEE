FROM java:8-jre
MAINTAINER RenFei <i@renfei.net>

ADD ./target/WinterEE-UAA-Serve.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/WinterEE-UAA-Serve.jar"]

EXPOSE 8114