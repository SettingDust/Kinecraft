plugins {
    alias(catalog.plugins.fabric.loom)
    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)
    `maven-publish`
}

base { archivesName.set("${rootProject.base.archivesName.get()}-${project.name}") }

dependencies {
    minecraft(catalog.minecraft.fabric)
    mappings(loom.officialMojangMappings())

    modImplementation(catalog.fabric.loader)
    implementation(catalog.kotlinx.serialization.core)
    implementation(catalog.kotlinx.serialization.json)
    implementation(catalog.kotlin.reflect)
    include(project(":fabric"))
}

tasks {
    remapJar {
        from("LICENSE") { rename { "${it}_KinecraftSerialization" } }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}