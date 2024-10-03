import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)
    alias(catalog.plugins.vanilla.gradle)
}

val mod_id: String by rootProject

java {
    toolchain { languageVersion = JavaLanguageVersion.of(21) }

    // Still required by IDEs such as Eclipse and Visual Studio Code
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build"
    // task if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    // If this mod is going to be a library, then it should also generate Javadocs in order to
    // aid with development.
    // Uncomment this line to generate them.
    withJavadocJar()
}

minecraft { version(catalog.versions.minecraft.get1().get21().get()) }

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
            "MixinConfigs" to "$mod_id.mixins.1.21.json"
        )
    }

    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
}