plugins {
    id("java")
    id("ps.spotless-conventions")
    id("ps.test-conventions")
}

group = "com.giwankim"
version = "0.0.1-SNAPSHOT"

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}
