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
    implementation(libs.ben.manes.plugin)
    implementation(libs.version.catalog.update.plugin)
    // Enable version catalog type-safe accessors in precompiled script plugins
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
