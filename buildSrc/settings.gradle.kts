@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
        gradlePluginPortal()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
