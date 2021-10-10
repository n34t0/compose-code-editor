pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":platform:api")
include(":platform:lib")
include(":editor")
include(":demo")
