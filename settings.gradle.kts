extra["minecraft"] = "1.20.1"

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/common.gradle.kts")

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/kotlin.gradle.kts")

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/fabric.gradle.kts")

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/forge.gradle.kts")

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/modmenu.gradle.kts")

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/vanillagradle.gradle.kts")

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/mixin.gradle.kts")


val mod_name: String by settings

rootProject.name = mod_name

dependencyResolutionManagement.versionCatalogs.named("catalog") {
    version("minecraft-1.21", "1.21")
    library("minecraft-fabric-1.21", "com.mojang", "minecraft").versionRef("minecraft-1.21")
    plugin("neoforge-moddev", "net.neoforged.moddev").version("1.+")

    // https://linkie.shedaniel.dev/dependencies?loader=neoforge
    library("neoforge", "net.neoforged", "neoforge").version("21.1.54")
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0" }

include("nested")

include("nested:nested-fabric")

include("nested:nested-forge")

include("common")

include("fabric")

include("lexforge")

include("neoforge")

include("versions:1.21")
include("versions:1.21:fabric-1.21")
include("versions:1.20")
include("versions:1.20:fabric-1.20")
include("versions:1.20:lexforge-1.20")
