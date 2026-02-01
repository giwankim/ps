plugins {
    id("de.fayard.refreshVersions") version "0.60.6"
}

rootProject.name = "ps"

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
    }
}

include("algospot")
include("aoc")
include("grind75")
include("leetcode")
include("leetcode-kt")
include("programmers")
