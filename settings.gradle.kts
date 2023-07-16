pluginManagement {
    repositories {
        maven {
            name = "quilt"
            url = uri("https://maven.quiltmc.org/repository/release")
        }
        maven {
            name = "fabricmc"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "Forge"
            url = uri("https://maven.minecraftforge.net/")
        }
        maven {
            name = "Sponge Snapshots"
            url = uri("https://repo.spongepowered.org/repository/maven-public/")
        }
        repositories {
            maven("https://maven.parchmentmc.org")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "kinecraft-serialization"
include("fabric")
include("forge")
