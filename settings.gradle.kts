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
include("boj")
include("grind75")
include("leetcode")
include("leetcode-kt")
include("programmers")
include("usaco")
include("company")
