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

sourceSets {
    main {
        java.srcDir(project(":versions:1.20").sourceSets.main.get().java)
        kotlin.srcDir(project(":versions:1.20").sourceSets.main.get().kotlin)
        resources.srcDir(project(":versions:1.20").sourceSets.main.get().resources)
    }
}

dependencies {
    minecraft(catalog.minecraft.forge)

    implementation(project(":versions:1.20"))
}
