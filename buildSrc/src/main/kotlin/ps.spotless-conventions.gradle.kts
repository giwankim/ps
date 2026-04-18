import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("com.diffplug.spotless")
}

val libs = the<LibrariesForLibs>()

spotless {
    java {
        toggleOffOn()
        removeUnusedImports()
        googleJavaFormat(libs.versions.google.java.format.get()).reorderImports(true)
        formatAnnotations()
    }
}
