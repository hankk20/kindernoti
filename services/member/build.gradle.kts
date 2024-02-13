import com.epages.restdocs.apispec.gradle.OpenApi3Extension
import com.epages.restdocs.apispec.gradle.OpenApi3Task
import com.epages.restdocs.apispec.gradle.PluginOauth2Configuration
import com.epages.restdocs.apispec.model.Oauth2Configuration
import groovy.lang.Closure
import org.jetbrains.kotlin.tooling.core.closure
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
extra["snippetsDir"] = file("build/generated-snippets")

dependencies {
	implementation(project(":services:core"))
	implementation(platform(libs.bom.spring.cloud))
	implementation(libs.bundles.spring.cloud)
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.cloud:spring-cloud-starter-config")


	implementation("org.keycloak:keycloak-admin-client:23.0.4")
	implementation("org.keycloak:keycloak-authz-client:23.0.4")
	implementation("commons-beanutils:commons-beanutils:1.9.4")
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
	implementation("org.springframework.session:spring-session-data-redis")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	//testcontainer
	implementation(platform("org.testcontainers:testcontainers-bom:1.19.1"))
	testImplementation("com.github.dasniko:testcontainers-keycloak:3.2.0")
	testImplementation("org.testcontainers:junit-jupiter")

	//Rest Docs
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.2.0")
	testImplementation(libs.bundles.restdoc)

	//lombok
	compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)
	testCompileOnly(libs.lombok)
	testAnnotationProcessor(libs.lombok)

	implementation(libs.lombok.mapstruct.binding)
	annotationProcessor(libs.lombok.mapstruct.binding)
	testAnnotationProcessor(libs.lombok.mapstruct.binding)

	//mapstructure
	implementation(libs.mapstruct)
	testImplementation(libs.mapstruct)
	annotationProcessor(libs.mapstruct.processor)
	testAnnotationProcessor(libs.mapstruct.processor)

}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("failed")
		setExceptionFormat("full")
		showCauses = true
		showStackTraces = true
	}
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
}

tasks.jacocoTestReport {
	reports {
		xml.required = true
		html.required = false
	}
}

openapi3 {
	setServer("http://kindernoti.co.kr/member-service")
	title = "회원 서비스"
	description = "회원 서비스 API"
	version = "1"
	format = "yaml"
	outputDirectory = layout.buildDirectory.dir("resources/main/static/docs").get().asFile.path
	setOauth2SecuritySchemeDefinition(closureOf<PluginOauth2Configuration> {
		tokenUrl = "http://auth.kindernoti.co.kr/realms/kindernoti/protocol/openid-connect/token"
		flows = arrayOf("authorizationCode", "password", "implicit")
		authorizationUrl = "http://auth.kindernoti.co.kr/realms/kindernoti/protocol/openid-connect/auth"
	} as Closure<PluginOauth2Configuration>)
}

tasks.withType<BootJar> {
	dependsOn(tasks.withType<OpenApi3Task>())
}

