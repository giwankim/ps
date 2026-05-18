import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("java-base")
    id("ps.test-conventions")
}

group = "com.giwankim"
version = "0.0.1-SNAPSHOT"

val libs = the<VersionCatalogsExtension>().named("libs")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.findVersion("java").get().requiredVersion)
    }
}
