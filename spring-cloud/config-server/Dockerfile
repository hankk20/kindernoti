FROM amazoncorretto:17-alpine3.18 as builder
WORKDIR application
ARG JAR_FILE=/spring-cloud/config-server/build/libs/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM amazoncorretto:17-alpine3.18
ENV server.port=8888
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]