import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
    signing
    id("org.jetbrains.compose") version "1.0.0-alpha4-build331"
}

group = group as String + ".compose"
version = getVersion("editor")
val platformVersion = getVersion("platform")

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    api(project(":platform:api"))
    implementation(project(":platform:lib")) {
        exclude("platform.build")
    }

    runtimeOnly("io.github.n34t0:platform-lib:${platformVersion}:idea")
    runtimeOnly("io.github.n34t0:platform-lib:${platformVersion}:java")
    runtimeOnly("io.github.n34t0:platform-lib:${platformVersion}:kotlin")
    runtimeOnly("io.github.n34t0:platform-lib:${platformVersion}:properties")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to "code-editor",
                "Implementation-Version" to project.version
            )
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenCodeEditor") {
            artifactId = "code-editor"

            from(components["java"])

            pom {
                name.set("Code Editor compose component")
                description.set("A code editor compose component")
                addCommonPom()
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenCodeEditor"])
}