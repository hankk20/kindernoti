import com.epages.restdocs.apispec.gradle.OpenApi3Task
import org.jetbrains.kotlin.cli.jvm.plugins.processCompilerPluginsOptions
import org.jetbrains.kotlin.fir.expressions.builder.buildAnnotation
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    id("com.epages.restdocs-api-spec") version "0.18.2" // epages plugin
    id("org.asciidoctor.jvm.convert") version "3.3.2"   // spring rest doc asciidoctor plugin
}

group = "kr.co.kindernoti"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.bouncycastle:bcprov-jdk18on:1.76")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.76")

    //querydsl
    implementation("com.querydsl:querydsl-mongodb:5.0.0") {
        exclude(group="org.mongodb", module="mongo-java-driver")
    }
    implementation("org.springframework.data:spring-data-mongodb")
    compileOnly("com.querydsl:querydsl-apt:5.0.0")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0")
    annotationProcessor("org.springframework.data:spring-data-mongodb")
    testAnnotationProcessor("com.querydsl:querydsl-apt:5.0.0")
    testAnnotationProcessor("org.springframework.data:spring-data-mongodb")


    //lombok
    compileOnly("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    //Rest Docs
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.2.0")
    testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient")
    testImplementation("com.epages:restdocs-api-spec-webtestclient:0.18.4")
    testImplementation("com.epages:restdocs-api-spec:0.18.4")

    //test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

openapi3 {
    setServer("http://localhost:8081")
    title = "Auth Service"
    description = "Auth Service API"
    version = "1"
    format = "yaml"
    outputDirectory = "${buildDir}/resources/main/static/docs"
}

tasks.withType<BootJar> {
    dependsOn(tasks.withType<OpenApi3Task>())
}
tasks.withType<JavaCompile> {
    options.compilerArgs.add("-processor")
    options.compilerArgs.add("org.springframework.data.mongodb.repository.support.MongoAnnotationProcessor,lombok.launch.AnnotationProcessorHider\$AnnotationProcessor")
}

