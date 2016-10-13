FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/youyesyet.jar /youyesyet/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/youyesyet/app.jar"]
