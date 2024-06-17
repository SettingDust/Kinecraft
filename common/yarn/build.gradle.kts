import net.fabricmc.loom.LoomGradleExtension
import net.fabricmc.loom.api.mappings.layered.MappingsNamespace
import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)
    alias(catalog.plugins.fabric.loom)
}

evaluationDependsOn(":common")

dependencies {
    minecraft(catalog.minecraft)
    mappings(variantOf(catalog.yarn) {
        classifier("v2")
    })
    implementation(project(":common"))
}

tasks {
    val yarnJar by creating(RemapJarTask::class.java) {
        classpath.from(LoomGradleExtension.get(project).getMinecraftJarsCollection(MappingsNamespace.OFFICIAL))

        val jar = project(":common").tasks.jar
        dependsOn(jar)

        inputFile.convention(jar.flatMap { it.archiveFile })

        sourceNamespace = ""
    }
}
