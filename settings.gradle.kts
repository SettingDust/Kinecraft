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
        gradlePluginPortal()
    }
}

rootProject.name = "minecraft-tag-serialization"
