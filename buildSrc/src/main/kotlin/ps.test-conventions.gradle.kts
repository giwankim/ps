plugins {
    java
}

dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.pioneer)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test> {
    useJUnitPlatform()
    // Online judges run solutions with large thread stacks, so recursive solutions that are
    // accepted there (e.g. DFS at depth 1e5) must not fail locally with StackOverflowError.
    jvmArgs("-Xss64m")
}
