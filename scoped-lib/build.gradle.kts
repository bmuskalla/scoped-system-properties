plugins {
    `java-library`
}

dependencies {
    testImplementation(enforcedPlatform("org.junit:junit-bom:5.5.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core:3.14.0")
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            artifactId = "scoped-system-properties"
            from(components["java"])
        }
    }
}