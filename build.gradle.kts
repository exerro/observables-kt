import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins { kotlin("jvm") version "1.6.10" }
repositories { mavenCentral() }
dependencies { implementation(kotlin("stdlib")) }

group = "com.exerro"
version = "1.1"

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-language-version"
    kotlinOptions.freeCompilerArgs += "1.7"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.experimental.ExperimentalTypeInference"
}
