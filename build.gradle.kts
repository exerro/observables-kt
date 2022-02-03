import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.6.10"
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-language-version"
    kotlinOptions.freeCompilerArgs += "1.7"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.experimental.ExperimentalTypeInference"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.exerro"
            artifactId = "observables"
            version = "1.1.0"

            from(components["java"])
        }
    }
}
