plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.spotless.plugin)
    implementation(libs.ktlint.plugin)
    implementation(libs.kotlin.plugin)
    implementation(libs.version.catalog.update.plugin)
}
