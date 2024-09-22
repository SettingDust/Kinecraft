plugins {
    `maven-publish`
    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)
    alias(catalog.plugins.vanilla.gradle)
}

minecraft { version(catalog.versions.minecraft.asProvider().get()) }

dependencies {
    api(catalog.kotlinx.serialization.core)
    api(catalog.kotlinx.serialization.json)
    api(catalog.kotlin.reflect)

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
