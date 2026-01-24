plugins {
    id("com.diffplug.spotless")
}

spotless {
    java {
        removeUnusedImports()
        googleJavaFormat().reorderImports(true)
    }
}
