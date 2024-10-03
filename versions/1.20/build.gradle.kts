plugins {
    `maven-publish`
    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)
    alias(catalog.plugins.vanilla.gradle)
}

val mod_id: String by rootProject

minecraft { version(catalog.versions.minecraft.asProvider().get()) }

dependencies {
    api(project(":common"))

    api(catalog.kotlinx.serialization.core)
    api(catalog.kotlinx.serialization.json)
    api(catalog.kotlin.reflect)
}

tasks {
    jar {
        manifest.attributes(
            "FMLModType" to "GAMELIBRARY",
            "MixinConfigs" to "$mod_id.mixins.1.20.json"
        )
    }
}