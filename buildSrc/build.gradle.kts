plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.spotless.plugin)
    implementation(libs.ktlint.plugin)
    implementation(libs.kotlin.plugin)
    // Enable version catalog type-safe accessors in precompiled script plugins
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
