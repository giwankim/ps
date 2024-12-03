import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("com.diffplug.spotless")
    id("org.jlleitschuh.gradle.ktlint")
    kotlin("jvm")
}

group = "${property("projectGroup")}"
version = "${property("projectVersion")}"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of("${property("javaVersion")}")
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:${property("junitJupiterVersion")}"))
    testImplementation(platform("org.assertj:assertj-bom:${property("assertJVersion")}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core")
    testImplementation("io.kotest:kotest-runner-junit5:${property("kotestVersion")}")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        // hardcoded JVM target version
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

tasks.test {
    useJUnitPlatform()
}

spotless {
    java {
        removeUnusedImports()
        googleJavaFormat().reflowLongStrings().reorderImports(true)
        formatAnnotations()
    }
}

tasks {
    ktlint {
        verbose.set(true)
    }
}
