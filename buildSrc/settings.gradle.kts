plugins {
    // Imports the main build's version catalog and generates typesafe `libs` accessors (including
    // `alias(libs.plugins.*)` with automatic plugin-marker dependencies) for the convention plugins.
    id("dev.panuszewski.typesafe-conventions") version "0.11.1"
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
        gradlePluginPortal()
        mavenCentral()
    }
}
