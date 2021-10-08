pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":ipw:api")
include(":ipw:lib")
include(":editor")
