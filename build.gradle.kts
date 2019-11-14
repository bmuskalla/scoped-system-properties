import org.gradle.api.tasks.testing.logging.TestLogEvent

version = "0.1.0"
group = "io.bmuskalla"

plugins {
    `java-library`
    `maven-publish`
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

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/bmuskalla/scoped-system-properties")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GPR_USER")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GPR_API_KEY")
            }
        }
    }
    publications {
        register("gpr",  MavenPublication::class) {
            from(components["java"])
        }
    }
}