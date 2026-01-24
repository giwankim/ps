import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

dependencies {
    "testImplementation"(libs.junit.jupiter)
    "testImplementation"(libs.junit.pioneer)
    "testImplementation"(libs.assertj.core)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
