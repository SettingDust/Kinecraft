import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)
    alias(catalog.plugins.fabric.loom)
}

loom {
    mixin {
        defaultRefmapName = "${rootProject.name}.refmap.json"
    }
}

dependencies {
    minecraft(catalog.minecraft.fabric)
    mappings(loom.officialMojangMappings())
    implementation(project(":common"))
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
        named<MavenPublication>("maven") {
            artifact(tasks.remapJar) {
                classifier = "fabric"
            }
        }
    }
}
