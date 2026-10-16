plugins {
    id("java")
    id("ps.base-conventions")
    id("ps.spotless-conventions")
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}
