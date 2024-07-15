plugins {
    id("java")
    id("com.diffplug.spotless") version "6.25.0"
}

group = "com.giwankim"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
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
