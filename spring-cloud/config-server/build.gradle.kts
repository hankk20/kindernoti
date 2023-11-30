plugins {
    id("kindernoti.java-base")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
}

group = "kr.co.kindernoti"

dependencies {
    implementation(platform(libs.bom.spring.cloud))
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-config-server")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder-jammy-base:latest")
}
