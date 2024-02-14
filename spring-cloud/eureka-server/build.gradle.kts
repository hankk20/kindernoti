plugins {
    id("kindernoti.java-base")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
}

group = "kr.co.kindernoti"

dependencies {
    implementation(platform(libs.bom.spring.cloud))
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
    implementation("org.springframework.cloud:spring-cloud-starter-config")

    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
