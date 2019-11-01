import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    testImplementation(enforcedPlatform("org.junit:junit-bom:5.5.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core:3.14.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    testLogging {
        showExceptions = true
        showStackTraces	= true
    }
}
