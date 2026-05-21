plugins {
    java
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    testImplementation(libs.findLibrary("junit-jupiter").get())
    testImplementation(libs.findLibrary("junit-pioneer").get())
    testImplementation(libs.findLibrary("assertj-core").get())
}

tasks.withType<Test> {
    useJUnitPlatform()
}
