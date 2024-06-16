plugins {
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.vanilla.gradle)
}

minecraft { version(libs.versions.minecraft.get()) }

dependencies {
    api(libs.kotlinx.serialization.core)
    api(libs.kotlinx.serialization.json)
    api(libs.kotlin.reflect)

    testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }

rootProject.publishing {
    publications {
        named<MavenPublication>("maven") {
            artifact(tasks.jar) {
                classifier = "common"
            }
        }
    }
}
