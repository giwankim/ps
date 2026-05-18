import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("ps.base-conventions")
    id("ps.ktlint-conventions")
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    testImplementation(libs.findLibrary("kotest").get())
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}
