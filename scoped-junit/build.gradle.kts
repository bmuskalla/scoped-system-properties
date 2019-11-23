plugins {
    `java-library`
}

dependencies {
    implementation(project(":scoped-lib"))
    implementation(enforcedPlatform("org.junit:junit-bom:5.5.2"))
    implementation("org.junit.jupiter:junit-jupiter-api")    
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core:3.14.0")
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            artifactId = "scoped-system-properties-junit"
            from(components["java"])
        }
    }
}