plugins {
    alias(libs.plugins.spotless)
}

spotless {
    java {
        toggleOffOn()
        removeUnusedImports()
        importOrder("java", "javax", "", "\\#")
        palantirJavaFormat(libs.versions.palantir.java.format.get())
            .style("GOOGLE")
            .formatJavadoc(true)
        formatAnnotations()
        trimTrailingWhitespace()
        endWithNewline()
    }
}
