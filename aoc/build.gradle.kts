plugins {
    id("ps.kotlin-conventions")
    alias(libs.plugins.benchmark)
    alias(libs.plugins.allopen)
}

dependencies {
    implementation(libs.benchmark)
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
    annotation("kotlinx.benchmark.State")
}

benchmark {
    targets {
        register("main")
    }
}
