plugins {
    kotlin("jvm") version "2.2.21"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-sensitive-resolution")
    }
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}

dependencies {
    implementation("com.google.ortools:ortools-java:9.12.4544")
}
