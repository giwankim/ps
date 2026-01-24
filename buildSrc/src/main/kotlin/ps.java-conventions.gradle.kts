plugins {
    id("java")
    id("ps.base-conventions")
    id("ps.spotless-conventions")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}
