plugins {
    `maven-publish`
    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)
    alias(catalog.plugins.vanilla.gradle)
}

minecraft { version(catalog.versions.minecraft.get1().get21().get()) }

dependencies {
    api(catalog.kotlinx.serialization.core)
    api(catalog.kotlinx.serialization.json)
    api(catalog.kotlin.reflect)
}
