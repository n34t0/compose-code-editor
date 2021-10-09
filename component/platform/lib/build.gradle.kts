plugins {
    `java-library`
    `maven-publish`
}

rootProject.apply {
    from(rootProject.file("gradle/projectProperties.gradle.kts"))
}

group = "com.github.n34t0"
version = rootProject.findProperty("platform.version") as String

repositories {
    buildLocalRepo(project)
    mavenLocal()
    mavenCentral()
}

dependencies {

    implementation(project(":platform:api"))

    implementation(intellijDep()) {includeJars(
        "platform-api",
        "platform-impl",
        "annotations",
        "bootstrap",
        "util",
        "kotlin-stdlib-jdk8",
        "external-system-rt"
    )}

    implementation(intellijPluginDep("java")) {includeJars(
        "java-api",
        "java-impl"
    )}

    implementation(intellijPluginDep("Kotlin")) {includeJars(
        "kotlin-core",
        "kotlin-idea",
        "kotlin-formatter"
    )}

    runtimeOnly(intellijDep()) {includeJars(
        "intellij-deps-fastutil-8.5.2-6",
        "intellij-xml",
        "intellij-dvcs",
        "kotlin-reflect-1.4.32",
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
    )}

    runtimeOnly(intellijPluginDep("java"))
    runtimeOnly(intellijPluginDep("Kotlin"))
    runtimeOnly(intellijPluginDep("properties"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

java {
    withSourcesJar()
}

tasks.compileJava {
    options.compilerArgs.add("--add-exports=java.desktop/sun.awt=ALL-UNNAMED")
    options.compilerArgs.addAll(listOf("-source",  "11", "-target", "11"))
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

tasks.processResources {
    from(intellijPlatformDir()) {
        include("build.txt")
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

tasks.build {
    dependsOn("packageDistribution")
}

publishing {
    publications {
        create<MavenPublication>("mavenPlatform") {
            artifactId = "platform-lib"
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
