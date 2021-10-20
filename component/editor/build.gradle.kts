import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "1.0.0-alpha4-build331"
    id("maven-publish")
}

group = "com.github.n34t0.compose"
version = "0.5.1"

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

    runtimeOnly("com.github.n34t0:platform-lib:0.5.1:idea")
    runtimeOnly("com.github.n34t0:platform-lib:0.5.1:java")
    runtimeOnly("com.github.n34t0:platform-lib:0.5.1:kotlin")
    runtimeOnly("com.github.n34t0:platform-lib:0.5.1:properties")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

java {
    withSourcesJar()
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
        create<MavenPublication>("codeEditor") {
            artifactId = "code-editor"
            from(components["java"])
        }
    }
}