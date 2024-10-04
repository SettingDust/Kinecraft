import net.minecraftforge.gradle.userdev.tasks.RenameJarInPlace
import org.apache.commons.io.FileUtils

plugins {
    `maven-publish`

    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)

    alias(catalog.plugins.forge.gradle)
    alias(catalog.plugins.mixin)
}

val mod_id: String by rootProject

minecraft { mappings("official", catalog.versions.minecraft.asProvider().get()) }

mixin {
    config("$mod_id.mixins.1.20.json")
    add(project(":versions:1.20").sourceSets.main.get(), "$mod_id.refmap.json")
}

dependencies { minecraft(catalog.minecraft.forge) }

tasks {
    jar {
        from(project(":versions:1.20").tasks.jar.flatMap { it.archiveFile }.map { zipTree(it) })
    }
}
