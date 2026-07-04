plugins {
    java
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    testImplementation(platform(libs.findLibrary("junit-bom").get()))
    testImplementation(libs.findLibrary("junit-jupiter").get())
    testImplementation(libs.findLibrary("junit-pioneer").get())
    testImplementation(libs.findLibrary("assertj-core").get())
    testRuntimeOnly(libs.findLibrary("junit-platform-launcher").get())
}

tasks.withType<Test> {
    useJUnitPlatform()
    // Online judges run solutions with large thread stacks, so recursive solutions that are
    // accepted there (e.g. DFS at depth 1e5) must not fail locally with StackOverflowError.
    jvmArgs("-Xss64m")
}
