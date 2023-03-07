FROM anapsix/alpine-java:8
WORKDIR /app
COPY target/unidappestablecimientoback-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 80
CMD java -jar /app/app.jar