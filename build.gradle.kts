plugins {
    id("ps.java-conventions")
    id("ps.kotlin-conventions")
    alias(libs.plugins.benchmark)
    alias(libs.plugins.allopen)
}

dependencies {
    implementation(libs.benchmark)
    testImplementation(libs.kotest)
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

benchmark {
    targets {
        register("main")
    }
}
