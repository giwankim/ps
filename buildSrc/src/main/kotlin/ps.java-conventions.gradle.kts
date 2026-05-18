import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("java")
    id("ps.base-conventions")
    id("ps.spotless-conventions")
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    compileOnly(libs.findLibrary("lombok").get())
    annotationProcessor(libs.findLibrary("lombok").get())
    testCompileOnly(libs.findLibrary("lombok").get())
    testAnnotationProcessor(libs.findLibrary("lombok").get())
}
