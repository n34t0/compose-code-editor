plugins {
    `java-library`
    `maven-publish`
}

rootProject.apply {
    from(rootProject.file("gradle/projectProperties.gradle.kts"))
}

group = "com.github.n34t0"
version = rootProject.findProperty("platform.version") as String

java {
    withSourcesJar()
}

tasks.compileJava {
    options.compilerArgs.addAll(listOf("-source",  "11", "-target", "11"))
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to "platform-api",
                "Implementation-Version" to project.version
            )
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenPlatform") {
            artifactId = "platform-api"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }
}

tasks.publish {
    dependsOn("jar", "sourcesJar", "publishMavenPlatformPublicationToMavenLocal")
}
