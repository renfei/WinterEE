FROM java:8-jre
MAINTAINER RenFei <i@renfei.net>

ADD ./target/WinterEE-Eureka-Serve.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/WinterEE-Eureka-Serve.jar"]

EXPOSE 8761