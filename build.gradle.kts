@file:Suppress("SpellCheckingInspection")

import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    implementation("com.google.guava:guava:28.0-jre")
    testImplementation(enforcedPlatform("org.junit:junit-bom:5.5.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core:3.14.0")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
        showStandardStreams = true
    }
}
