FROM eclipse-temurin:21-jdk AS build
WORKDIR /src
COPY src/ ./
RUN javac -d /out CatalogoLocalServer.java CatalogoHealthCheck.java \
    && cp feriados.json /out/

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /out/ /app/
EXPOSE 8081

CMD ["java", "-cp", "/app", "CatalogoLocalServer", "--host", "0.0.0.0"]
