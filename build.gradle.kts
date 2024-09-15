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
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation(platform("org.junit:junit-bom:${property("junitJupiterVersion")}"))
    testImplementation(platform("org.assertj:assertj-bom:${property("assertJVersion")}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "${project.property("javaVersion")}"
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
