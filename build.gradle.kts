plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    jacoco
    id("info.solidsoft.pitest") version "1.15.0"
}

group = "com.example.books"
version = "1.0.0"

repositories {
    mavenCentral()
}

val kotestVersion = "5.9.1"
val archunitVersion = "1.3.0"
val cucumberVersion = "7.18.0"

sourceSets {
    create("testIntegration") {
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    }
    create("testComponent") {
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    }
    create("testArchitecture") {
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    }
}

val testIntegrationImplementation by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}
val testComponentImplementation by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}
val testArchitectureImplementation by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.liquibase:liquibase-core")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.mockk:mockk:1.13.11")

    testIntegrationImplementation("org.springframework.boot:spring-boot-starter-test")
    testIntegrationImplementation("com.ninja-squad:springmockk:4.0.2")

    testComponentImplementation("org.springframework.boot:spring-boot-starter-test")
    testComponentImplementation("io.cucumber:cucumber-java8:$cucumberVersion")
    testComponentImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    testComponentImplementation("io.cucumber:cucumber-spring:$cucumberVersion")
    testComponentImplementation("org.junit.platform:junit-platform-suite")

    testArchitectureImplementation("com.tngtech.archunit:archunit-junit5:$archunitVersion")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

val testIntegration = tasks.register<Test>("testIntegration") {
    description = "Runs integration tests."
    group = "verification"
    testClassesDirs = sourceSets["testIntegration"].output.classesDirs
    classpath = sourceSets["testIntegration"].runtimeClasspath
    useJUnitPlatform()
    shouldRunAfter(tasks.test)
}

val testComponent = tasks.register<Test>("testComponent") {
    description = "Runs component (Cucumber) tests."
    group = "verification"
    testClassesDirs = sourceSets["testComponent"].output.classesDirs
    classpath = sourceSets["testComponent"].runtimeClasspath
    useJUnitPlatform()
    shouldRunAfter(tasks.test)
}

val testArchitecture = tasks.register<Test>("testArchitecture") {
    description = "Runs architecture tests."
    group = "verification"
    testClassesDirs = sourceSets["testArchitecture"].output.classesDirs
    classpath = sourceSets["testArchitecture"].runtimeClasspath
    useJUnitPlatform()
    shouldRunAfter(tasks.test)
}

tasks.check {
    dependsOn(testIntegration, testComponent, testArchitecture)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.required = true
        csv.required = false
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}

pitest {
    junit5PluginVersion = "1.2.1"
    targetClasses = setOf("com.example.books.domain.*")
    targetTests = setOf("com.example.books.domain.*")
    threads = 2
    outputFormats = setOf("XML", "HTML")
    mutationThreshold = 80
    coverageThreshold = 80
    timestampedReports = false
}

detekt {
    buildUponDefaultConfig = true
    autoCorrect = false
}

kotlin {
    jvmToolchain(17)
}
