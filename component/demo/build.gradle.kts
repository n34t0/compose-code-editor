import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.0.0-alpha4-build331"
}

kotlin {
    jvm {}

    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":platform:lib"))
                implementation(project(":editor"))
            }
        }
    }
}

rootProject.apply {
    from(rootProject.file("gradle/projectProperties.gradle.kts"))
}

repositories {
    buildLocalRepo(project)
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

compose.desktop {
    application {
        mainClass = "com.github.n34t0.compose.codeEditor.demo.MainKt"
        jvmArgs("-Xmx2048m")
//        jvmArgs("-Xmx2048m", "-Dplatform.stub=true")

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "sample"
            packageVersion = "1.0.0"
        }
    }
}
