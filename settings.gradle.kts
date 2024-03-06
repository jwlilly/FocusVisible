pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        repositories {
            maven {
                url = uri("https://jitpack.io")
            }
        }
    }
}

rootProject.name = "Focus Visible"
include(":app")
 