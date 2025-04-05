FROM amazoncorretto:21.0.6-alpine

WORKDIR /app

COPY build/libs/app.jar ./app.jar

CMD ["java", "-jar", "app.jar"]

