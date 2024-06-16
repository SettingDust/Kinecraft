import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.neoforge.gradle)
    alias(libs.plugins.neoforge.gradle.mixin)
}

mixin {
    config("${rootProject.name}.mixins.json")
}

dependencies {
    implementation(libs.neoforge)
    implementation(project(":common"))
}

tasks {
    withType<ProcessResources> { from(project(":common").sourceSets.main.get().resources) }
    withType<KotlinCompile> { source(project(":common").sourceSets.main.get().allSource) }
    withType<JavaCompile> { source(project(":common").sourceSets.main.get().allSource) }

    sourcesJar { from(project(":common").sourceSets.main.get().allSource) }

    jar {
        manifest {
            "FMLModType" to "GAMELIBRARY"
            "Automatic-Module-Name" to project.path
        }
    }
}
