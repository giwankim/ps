plugins {
    id("ps.java-conventions")
}

// Some BOJ solutions (e.g. boj15486) use deep recursion whose depth scales with N (up to
// 1,500,000). The default test-worker thread stack (~2 MB on this JDK) overflows well before that,
// so give the forked test JVM a larger stack. Note this only affects local/CI test runs -- it does
// not travel with a submission to the judge.
tasks.withType<Test> {
    jvmArgs("-Xss128m")
}
