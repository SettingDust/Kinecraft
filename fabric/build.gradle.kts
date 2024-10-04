import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)
    alias(catalog.plugins.fabric.loom)
}

val mod_id: String by rootProject

loom {
    mixin { defaultRefmapName = "$mod_id.refmap.json" }
    runs { named("client") { ideConfigGenerated(true) } }
}

dependencies {
    minecraft(catalog.minecraft.fabric)
    mappings(loom.officialMojangMappings())

    modImplementation(catalog.fabric.loader)
    modImplementation(catalog.fabric.api)
    modImplementation(catalog.fabric.kotlin)

    modRuntimeOnly(catalog.modmenu)

    implementation(project(":common"))

    include(project(":versions:1.21:fabric-1.21"))
    include(project(":versions:1.20:fabric-1.20"))
}

tasks {
    ideaSyncTask { enabled = true }

    withType<ProcessResources> { from(project(":common").sourceSets.main.get().resources) }
    withType<KotlinCompile> { source(project(":common").sourceSets.main.get().allSource) }
    withType<JavaCompile> { source(project(":common").sourceSets.main.get().allSource) }

    sourcesJar { from(project(":common").sourceSets.main.get().allSource) }
}

rootProject.publishing {
    publications {
        named<MavenPublication>("maven") { artifact(tasks.remapJar) { classifier = "fabric" } }
    }
}
