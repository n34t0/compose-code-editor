plugins {
    `java-library`
    `maven-publish`
    signing
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
        create<MavenPublication>("mavenPlatformApi") {
            artifactId = "platform-api"

            from(components["java"])

            pom {
                name.set("Code Editor Platform API")
                description.set("A library that provides an API for using code completion " +
                    "and go-to-definition functions using the IntelliJ platform")
                addCommonPom()
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenPlatformApi"])
}