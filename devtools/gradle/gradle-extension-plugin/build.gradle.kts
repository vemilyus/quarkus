plugins {
    id("io.quarkus.devtools.gradle-plugin")
}

dependencies {
    // Necessary to get the correct Kapt configuration name to add the annotation processor to
    compileOnly(libs.kotlin.gradle.plugin)

    implementation(libs.jackson.databind)
    implementation(libs.jackson.dataformat.yaml)
}

group = "io.quarkus.extension"

gradlePlugin {
    plugins.create("quarkusBootstrapPlugin") {
        id = "io.quarkus.extension"
        implementationClass = "io.quarkus.extension.gradle.QuarkusExtensionPlugin"
        displayName = "Quarkus Extension Plugin"
        description = "Builds a Quarkus extension"
        tags.addAll("quarkus", "quarkusio", "graalvm")
    }
}

tasks.withType<Test>().configureEach {
    environment("GITHUB_REPOSITORY", "some/repo")
}
