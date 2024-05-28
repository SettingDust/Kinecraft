pluginManagement {
    repositories {
        maven("https://maven.architectury.dev/")
        maven("https://maven.quiltmc.org/repository/release") { name = "Quilt" }
        // Currently needed for Intermediary and other temporary dependencies
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        maven("https://maven.neoforged.net/releases") { name = "NeoForge" }
        maven("https://repo.spongepowered.org/repository/maven-public/") {
            name = "Sponge Snapshots"
        }
        gradlePluginPortal()
    }
}

rootProject.name = "kinecraft-serialization"

include("fabric")

include("forge")
