val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

dependencies {
    "testImplementation"(libs.junit.jupiter)
    "testImplementation"(libs.junit.pioneer)
    "testImplementation"(libs.assertj.core)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
