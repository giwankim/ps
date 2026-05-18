import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("com.diffplug.spotless")
}

val libs = the<VersionCatalogsExtension>().named("libs")

spotless {
    java {
        toggleOffOn()
        removeUnusedImports()
        importOrder("java", "javax", "", "\\#")
        palantirJavaFormat(libs.findVersion("palantir-java-format").get().requiredVersion).style("GOOGLE")
        formatAnnotations()
        trimTrailingWhitespace()
        endWithNewline()
    }
}
