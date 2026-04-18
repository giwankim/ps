import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("java")
    id("ps.base-conventions")
    id("ps.spotless-conventions")
}

val libs = the<LibrariesForLibs>()

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}
