plugins {
    id("ps.java-conventions")
}

tasks.withType<Test> {
    jvmArgs("-Xss256m")
}
