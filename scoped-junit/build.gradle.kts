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

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("sonatype") {
            artifactId = "scoped-system-properties-junit"
            from(components["java"])

            pom {
                name.set("Scoped System Properties JUnit Extension")
                url.set("https://github.com/bmuskalla/scoped-system-properties")
                description.set("JUnit 5 Extension to isolate changes to system properties per test")
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("http://www.apache.org/licenses/")
                    }
                }
                developers {
                    developer {
                        id.set("bmuskalla")
                        name.set("Benjamin Muskalla")
                        email.set("b.muskalla@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:bmuskalla/scoped-system-properties.git")
                    url.set("https://github.com/bmuskalla/scoped-system-properties")
                }
                issueManagement {
                    url.set("https://github.com/bmuskalla/scoped-system-properties/issues")
                    system.set("GitHub")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["sonatype"])
}