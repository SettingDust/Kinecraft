plugins {
    `maven-publish`

    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)

    alias(catalog.plugins.forge.gradle)
}

val mod_id: String by rootProject

minecraft { mappings("official", "${catalog.versions.minecraft.get()}") }

base { archivesName.set("${rootProject.base.archivesName.get()}-${project.name}") }

jarJar.enable()

dependencies {
    minecraft(catalog.minecraft.forge)
    implementation(catalog.kotlinx.serialization.core)
    implementation(catalog.kotlinx.serialization.json)
    implementation(catalog.kotlin.reflect)
    jarJar(project(":lexforge"))
}

tasks {
    jar {
        from("LICENSE") { rename { "${it}_KinecraftSerialization" } }
        manifest.attributes("FMLModType" to "GAMELIBRARY")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    sourcesJar { from(rootProject.sourceSets.main.get().allSource) }
}

publishing {
    publications {
        create<MavenPublication>(rootProject.name) {
            groupId = "${rootProject.group}"
            artifactId = base.archivesName.get()
            version = "${rootProject.version}"
            from(components.getByName("java"))
        }
    }
}
