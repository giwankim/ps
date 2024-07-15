plugins {
    id("java")
    id("com.diffplug.spotless")
    id("org.jlleitschuh.gradle.ktlint")
    kotlin("jvm")
}

group = "${property("projectGroup")}"
version = "${property("projectVersion")}"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation(platform("org.junit:junit-bom:${property("junitJupiterVersion")}"))
    testImplementation(platform("org.assertj:assertj-bom:${property("assertJVersion")}"))
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
