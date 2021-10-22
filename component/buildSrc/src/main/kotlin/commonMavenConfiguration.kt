import org.gradle.api.publish.maven.MavenPom

fun MavenPom.addCommonPom() {
    url.set("https://github.com/n34t0/compose-code-editor")
    licenses {
        license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
        }
    }
    developers {
        developer {
            id.set("n34t0")
            name.set("Alex Hosh")
            email.set("n34to0@gmail.com")
        }
    }
    scm {
        connection.set("scm:git:git://github.com/n34t0/compose-code-editor.git")
        developerConnection.set("scm:git:ssh://github.com:n34t0/compose-code-editor.git")
        url.set("https://github.com/n34t0/compose-code-editor/tree/master")
    }
}