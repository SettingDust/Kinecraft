import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.forge.gradle)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.minotaur)
}

base {
    archivesName.set("kinecraft_serialization-forge")
}

repositories {
    mavenCentral()
}

minecraft {
    mappings("official", libs.versions.minecraft.get())
}

dependencies {
    minecraft(libs.forge)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.hocon)
    implementation(libs.kotlin.reflect)

//    jarJar(rootProject)
}

tasks {
    withType<KotlinCompile> {
        source(rootProject.sourceSets.main.get().allSource)
    }
    jar {
        from("LICENSE") {
            rename { "${it}_MixinExtras" }
        }
        finalizedBy("reobfJar")
        manifest.attributes(
            "FMLModType" to "GAMELIBRARY",
        )
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    sourcesJar {
        from(rootProject.sourceSets.main.get().allSource)
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN")) // This is the default. Remember to have the MODRINTH_TOKEN environment variable set or else this will fail, or set it to whatever you want - just make sure it stays private!
    projectId.set("kinecraft-serialization") // This can be the project ID or the slug. Either will work!
    syncBodyFrom.set(rootProject.file("README.md").readText())
    versionType.set("release") // This is the default -- can also be `beta` or `alpha`
    uploadFile.set(tasks.jar)
    versionNumber.set("${project.version}-forge")
    gameVersions.addAll(
        "1.18.2",
        "1.19",
        "1.19.1",
        "1.19.2",
        "1.19.3",
        "1.19.4",
        "1.20",
        "1.20.1",
    ) // Must be an array, even with only one version
    loaders.add(
        "forge",
    ) // Must also be an array - no need to specify this if you're using Loom or ForgeGradle
    dependencies {
        required.project("ordsPcFz")
    }
}
