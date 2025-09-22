plugins {
    id("java")
    alias(libs.plugins.spotless)
}

group = "com.giwankim"
version = "0.0.1-SNAPSHOT"

dependencies {
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.pioneer)
    testImplementation(libs.assertj.core)
}

tasks.test {
    useJUnitPlatform()
}

spotless {
    java {
        googleJavaFormat().reorderImports(true)
    }
}
