FROM amazoncorretto:17-alpine3.18 as builder
WORKDIR application
ARG JAR_FILE=/api-gateway/build/libs/*.jar
COPY ${JAR_FILE} api-gateway.jar
RUN java -Djarmode=layertools -jar api-gateway.jar extract

FROM amazoncorretto:17-alpine3.18
ENV server.port=8090
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]