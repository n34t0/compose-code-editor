import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

rootProject.apply {
    from(rootProject.file("gradle/projectProperties.gradle.kts"))
}

repositories {
    buildLocalRepo(project)
    mavenLocal()
    mavenCentral()
}

dependencies {
    api(project(":platform:api"))

    implementation(intellijDep(), {
        isTransitive = false
        includeJars(
            "platform-api",
            "platform-impl",
            "annotations",
            "bootstrap",
            "util",
            "kotlin-stdlib-jdk8",
            "external-system-rt"
        )
    })

    implementation(intellijPluginDep("java")) {
        isTransitive = false
        includeJars(
            "java-api",
            "java-impl"
        )
    }

    implementation(intellijPluginDep("Kotlin")) {
        isTransitive = false
        includeJars(
            "kotlin-core",
            "kotlin-idea",
            "kotlin-formatter"
        )
    }

    runtimeOnly(intellijDep()) {
        isTransitive = false
        includeJars(
            "intellij-deps-fastutil-8.5.2-6",
            "intellij-xml",
            "intellij-dvcs",
            "resources",
            "resources_en",
            "log4j",
            "trove4j",
            "jdom",
            "jps-model",
            "proxy-vole",
            "caffeine-2.8.8",
            "jna",
            "jna-platform",
            "nanoxml-2.2.3",
            "json",
            "stats",
            "guava",
            "ap-validation-0.0.5",
            "streamex-0.7.3",
            "lz4-java-1.7.1",
            "forms_rt",
            "dom-openapi",
            "dom-impl",
            "asm-7.1",
            "asm-all-9.1",
            "oro-2.0.8",
            "cglib-nodep-3.2.4",
            "netty-codec-http",
            "netty-buffer",
            "protobuf-java-3.13.0",
            "automaton-1.12-1",
            "java-compatibility-1.0.1",
            "jackson-core-2.12.0",
            "jackson-databind",
            "gson-2.8.6",
            "jsp-base-openapi",
            "testFramework"
        )
    }

    runtimeOnly(intellijPluginDep("java")) {
        isTransitive = false
    }
    runtimeOnly(intellijPluginDep("Kotlin")) {
        isTransitive = false
    }
    runtimeOnly(intellijPluginDep("properties")) {
        isTransitive = false
    }

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.compileJava {
    options.compilerArgs.add("--add-exports=java.desktop/sun.awt=ALL-UNNAMED")
}

tasks.processResources {
    from(intellijPlatformDir()) {
        include("build.txt", "lib/jdkAnnotations.jar")
        into("home")
    }
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to "platform-lib",
                "Implementation-Version" to project.version
            )
        )
    }
}

tasks.javadoc {
    exclude("com/intellij/**")
}

tasks.test {
    useJUnitPlatform()
    systemProperty("ipw.debug", "true")
    jvmArgs(
        "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
        "--add-opens=java.desktop/sun.font=ALL-UNNAMED",
        "--add-opens=java.desktop/sun.swing=ALL-UNNAMED",
        "--add-opens=java.desktop/java.awt=ALL-UNNAMED",
        "--add-opens=java.desktop/java.awt.event=ALL-UNNAMED",
        "--add-opens=java.desktop/javax.swing=ALL-UNNAMED",
        "--add-opens=java.desktop/javax.swing.plaf.basic=ALL-UNNAMED"
    )
}

val plugins = listOf(
    "idea" to "ideaIC",
    "java" to "java",
    "kotlin" to "Kotlin",
    "properties" to "properties"
)

plugins.forEach { (archiveName, pluginName) ->
    tasks.register<ShadowJar>("${archiveName}Jar") {
        archiveClassifier.set(archiveName)
        dependencies {
            include {
                it.moduleGroup == "platform.build" && it.moduleName == pluginName
            }
        }
    }
}

tasks.withType<ShadowJar> {
    manifest {
        from(tasks.jar.get().manifest)
    }

    isZip64 = true
    mergeServiceFiles()
    append("META-INF/io.netty.versions.properties")
    configurations = listOf(project.configurations.runtimeClasspath.get())
    exclude("META-INF/jb/$\$size$$")
    exclude("__packageIndex__")
    exclude(".hash")
    exclude("**/package-info.class")
    exclude("**/module-info.class")

    exclude("META-INF/DEPENDENCIES")
    exclude("META-INF/AL2.0")
    exclude("META-INF/LGPL2.1")
    exclude("META-INF/LICENSE")
    exclude("META-INF/LICENSE.txt")
    exclude("META-INF/NOTICE")
    exclude("META-INF/NOTICE.txt")

    exclude("META-INF/INDEX.LIST")
    exclude("META-INF/*.RSA")
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
}

tasks.register<Zip>("packageDistribution") {
    archiveFileName.set("platform-lib.zip")
    destinationDirectory.set(layout.buildDirectory.dir("dist"))

    from(tasks.jar)
    from(configurations.runtimeClasspath.get()) {
        val repoPath = intellijPlatformDir().toPath()
        eachFile {
            path = repoPath.relativize(file.toPath()).toString()
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenPlatformLib") {
            artifactId = "platform-lib"

            artifact(tasks["jar"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            plugins.forEach { (archiveName, _) ->
                artifact(tasks["${archiveName}Jar"])
            }

            pom {
                name.set("Code Editor Platform Library")
                description.set("A library that provides code completion " +
                    "and go-to-definition functions using the IntelliJ platform")
                addCommonPom()
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenPlatformLib"])
}