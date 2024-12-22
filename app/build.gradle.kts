plugins {
    java
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // spring
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    // This dependency is used by the application.
    implementation(libs.guava)
    implementation("org.glassfish.jaxb:codemodel:4.0.5")

    // Use JUnit Jupiter for testing.
    testImplementation("org.mockito:mockito-junit-jupiter:5.14.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.0")
    testImplementation("org.mockito:mockito-core:5.14.2")
    testImplementation("org.simplify4u:slf4j-mock:2.4.0")
    testImplementation("org.slf4j:slf4j-api:1.7.36")
    testImplementation("ch.qos.logback:logback-core:1.5.12")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
