plugins {
    kotlin("jvm") version "1.9.23"
    jacoco
    id("info.solidsoft.pitest") version "1.15.0"
}

group = "com.example.books"
version = "1.0.0"

repositories {
    mavenCentral()
}

val kotestVersion = "5.9.1"

dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.mockk:mockk:1.13.11")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
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
    targetClasses = setOf("com.example.books.*")
    targetTests = setOf("com.example.books.*")
    threads = 2
    outputFormats = setOf("XML", "HTML")
    mutationThreshold = 80
    coverageThreshold = 80
    timestampedReports = false
}

kotlin {
    jvmToolchain(17)
}
