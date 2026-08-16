plugins {
    id("ps.java-conventions")
}

// AtCoder's judge (Language Test 202505, live since 2025-10-18) compiles submissions with
// OpenJDK 24, one release behind this repo's Java 25 toolchain. Pin this module to 24 so a
// Java 25-only feature fails at compile time here rather than coming back as a CE from the judge.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}
