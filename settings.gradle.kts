extra["minecraft"] = "1.21"

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/common.gradle.kts")
apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/kotlin.gradle.kts")
apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/fabric.gradle.kts")
apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/neoforge.gradle.kts")
apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/modmenu.gradle.kts")
apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/vanillagradle.gradle.kts")

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
