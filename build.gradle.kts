allprojects {
    version = "0.1.0"
    group = "io.bmuskalla"

    repositories {
        jcenter()
    }

    apply(plugin = "java-library")
    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    apply(plugin = "maven-publish")
    configure<PublishingExtension> {
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
    }
    
    val test by tasks.getting(Test::class) {
        useJUnitPlatform()
        testLogging {
            showExceptions = true
            showStackTraces	= true
        }
    }

}