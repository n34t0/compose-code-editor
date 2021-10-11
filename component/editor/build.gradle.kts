import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.0.0-alpha4-build331"
}

kotlin {
    jvm("desktop")

    sourceSets {
        named("commonMain") {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.materialIconsExtended)
                api(project(":platform:api"))
            }
        }
        named("desktopMain") {
            dependencies {
                implementation(compose.desktop.common)
                implementation(project(":platform:lib"))
            }
        }
    }
}

rootProject.apply {
    from(rootProject.file("gradle/projectProperties.gradle.kts"))
}

version = "0.5.0"

repositories {
    buildLocalRepo(project)
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
