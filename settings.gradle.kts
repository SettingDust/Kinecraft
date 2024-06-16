pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.architectury.dev/")
        maven("https://maven.quiltmc.org/repository/release") { name = "Quilt" }
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        maven("https://maven.neoforged.net/releases") { name = "NeoForge" }
        maven("https://repo.spongepowered.org/repository/maven-public/") {
            name = "Sponge Snapshots"
        }
    }
}

rootProject.name = "kinecraft_serialization"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("fabric")

include("neoforge")

include("common")
include("common:fabricTransform")
include("common:neoforgeTransform")
