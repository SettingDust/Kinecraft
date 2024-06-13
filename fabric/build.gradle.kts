plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.minotaur)
    `maven-publish`
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())

    modImplementation(libs.fabric.loader)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.reflect)
    include(project(":common:fabricTransform"))
}

tasks {
    remapJar {
        from("LICENSE") { rename { "${it}_KinecraftSerialization" } }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

modrinth {
    token.set(
        System.getenv("MODRINTH_TOKEN")
    ) // This is the default. Remember to have the MODRINTH_TOKEN environment variable set or else
    // this will fail, or set it to whatever you want - just make sure it stays private!
    projectId.set(
        "kinecraft-serialization"
    ) // This can be the project ID or the slug. Either will work!
    syncBodyFrom.set(rootProject.file("README.md").readText())
    versionType.set("release") // This is the default -- can also be `beta` or `alpha`
    uploadFile.set(tasks.remapJar)
    versionNumber.set("${project.version}")
    changelog = rootProject.file("CHANGELOG.md").readText()
    gameVersions.addAll(
        "1.16.5",
        "1.18.2",
        "1.19",
        "1.19.1",
        "1.19.2",
        "1.19.3",
        "1.19.4",
        "1.20",
        "1.20.1",
        "1.20.2",
        "1.20.3",
        "1.20.4",
        "1.20.5",
        "1.20.6"
    ) // Must be an array, even with only one version
    loaders.addAll(
        "fabric"
    ) // Must also be an array - no need to specify this if you're using Loom or ForgeGradle
    dependencies { required.project("fabric-language-kotlin") }
}

publishing {
    publications {
        create<MavenPublication>("kinecraft-serialization") {
            groupId = "${rootProject.group}"
            artifactId = base.archivesName.get()
            version = "${rootProject.version}"
            from(components.getByName("java"))
        }
    }
}
