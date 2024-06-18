extra["minecraft"] = "1.21"

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/common.gradle.kts")
apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/kotlin.gradle.kts")
apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/fabric.gradle.kts")
apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/neoforge.gradle.kts")
apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/modmenu.gradle.kts")

pluginManagement {
    repositories {
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }
}

dependencyResolutionManagement.versionCatalogs.named("catalog") {
    plugin("vanilla-gradle", "org.spongepowered.gradle.vanilla").version("0.2.1-SNAPSHOT")
    library("mixin", "org.spongepowered", "mixin").version("0.8.5")
}

rootProject.name = "kinecraft_serialization"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}


include("nested")
include("nested:nested-fabric")
include("nested:nested-neoforge")

include("common")
include("fabric")
include("neoforge")
