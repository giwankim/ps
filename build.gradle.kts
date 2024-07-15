plugins {
    id("java")
    id("com.diffplug.spotless") version "6.25.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    kotlin("jvm")
}

group = "com.giwankim"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation(platform("org.assertj:assertj-bom:3.26.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core")
}

tasks.test {
    useJUnitPlatform()
}

spotless {
    java {
        removeUnusedImports("google-java-format")
        googleJavaFormat().reflowLongStrings().reorderImports(true)
        formatAnnotations()
    }
}

kotlin {
    jvmToolchain(21)
}
