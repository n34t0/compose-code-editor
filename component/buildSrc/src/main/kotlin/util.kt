import org.gradle.api.Project

fun Project.getVersion(moduleName: String) =
    (property("${moduleName}.version") as String) + if (!hasProperty("release")) "-SNAPSHOT" else ""
