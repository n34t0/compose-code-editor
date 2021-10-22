plugins {
    id("io.codearte.nexus-staging") version "0.30.0"
}

nexusStaging {
    serverUrl = "https://s01.oss.sonatype.org/service/local/"
    packageGroup = "io.github.n34t0"
}

subprojects {
    group = "io.github.n34t0"

    plugins.withId("java") {
        extensions.getByType(JavaPluginExtension::class.java).apply {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11

            withJavadocJar()
            withSourcesJar()
        }
    }

    plugins.withId("maven-publish") {
        extensions.getByType(PublishingExtension::class.java).apply {
            repositories {
                maven {
                    val releaseRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                    val snapshotRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                    url = uri(if (hasProperty("release")) releaseRepoUrl else snapshotRepoUrl)
                    credentials {
                        username = (findProperty("nexusUsername") ?: "").toString()
                        password = (findProperty("nexusPassword") ?: "").toString()
                    }
                }
            }
        }
    }

    tasks.withType<Sign>().configureEach {
        onlyIf {
            gradle.taskGraph.allTasks.any { it.name.endsWith("publish") }
        }
    }
}