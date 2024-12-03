rootProject.name = "ps"

pluginManagement {
    val kotlinVersion: String by settings
    val spotlessVersion: String by settings
    val ktlintVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
                "com.diffplug.spotless" -> useVersion(spotlessVersion)
                "org.jlleitschuh.gradle.ktlint" -> useVersion(ktlintVersion)
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
