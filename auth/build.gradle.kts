import com.epages.restdocs.apispec.gradle.OpenApi3Task
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("kindernoti.java-base")
    jacoco
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
    alias(libs.plugins.epages)
    alias(libs.plugins.asciidoctor)
}

group = "kr.co.kindernoti"

dependencies {
    implementation(platform(libs.bom.spring.cloud))
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.projectreactor.kafka:reactor-kafka:1.3.21")
    implementation(libs.bundles.bouncycastle)

    //querydsl
    implementation("com.querydsl:querydsl-mongodb:${libs.versions.querydsl.get()}") {
        exclude(group="org.mongodb", module="mongo-java-driver")
    }
    implementation("org.springframework.data:spring-data-mongodb")
    implementation("com.querydsl:querydsl-apt:${libs.versions.querydsl.get()}")
    annotationProcessor("com.querydsl:querydsl-apt:${libs.versions.querydsl.get()}")
    annotationProcessor("org.springframework.data:spring-data-mongodb")
    testAnnotationProcessor("com.querydsl:querydsl-apt:${libs.versions.querydsl.get()}")
    testAnnotationProcessor("org.springframework.data:spring-data-mongodb")


    //lombok
    compileOnly("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    //Rest Docs
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.2.0")
    testImplementation(libs.bundles.restdoc)

    //test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")

//    //testcontainer
    implementation(platform("org.testcontainers:testcontainers-bom:1.19.1"))
    testImplementation("org.testcontainers:mongodb")
    testImplementation("org.testcontainers:junit-jupiter")
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        html.required = false
    }
}

openapi3 {
    setServer("http://localhost:8081")
    title = "Auth Service"
    description = "Auth Service API"
    version = "1"
    format = "yaml"
    outputDirectory = layout.buildDirectory.dir("resources/main/static/docs").get().asFile.path
}

tasks.withType<BootJar> {
    dependsOn(tasks.withType<OpenApi3Task>())
}
tasks.withType<JavaCompile> {
    options.compilerArgs.add("-processor")
    options.compilerArgs.add("org.springframework.data.mongodb.repository.support.MongoAnnotationProcessor,lombok.launch.AnnotationProcessorHider\$AnnotationProcessor")
}

