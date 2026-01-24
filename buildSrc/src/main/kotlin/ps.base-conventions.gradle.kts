import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("java-base")
    id("ps.test-conventions")
}

group = "com.giwankim"
version = "0.0.1-SNAPSHOT"

val libs = the<LibrariesForLibs>()

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}
