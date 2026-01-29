import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("ps.base-conventions")
    id("ps.ktlint-conventions")
}

val libs = the<LibrariesForLibs>()

dependencies {
    testImplementation(libs.kotest)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}
