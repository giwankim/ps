import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("com.diffplug.spotless")
}

val libs = the<LibrariesForLibs>()

spotless {
    java {
        toggleOffOn()
        removeUnusedImports()
        palantirJavaFormat(libs.versions.palantir.java.format.get()).style("GOOGLE")
        formatAnnotations()
    }
}
