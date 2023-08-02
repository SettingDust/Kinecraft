import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.minotaur)
    `maven-publish`
}

base {
    archivesName.set("kinecraft_serialization-fabric")
}

repositories {
    mavenCentral()
}

dependencies {
    minecraft(libs.minecraft)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.hocon)
    implementation(libs.kotlin.reflect)

    mappings(loom.officialMojangMappings())

//    include(rootProject)
}

tasks {
    withType<KotlinCompile> {
        source(rootProject.sourceSets.main.get().allSource)
    }
    remapJar {
        from("LICENSE") {
            rename { "${it}_MixinExtras" }
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    sourcesJar {
        from(rootProject.sourceSets.main.get().allSource)
    }
}
modrinth {
    token.set(System.getenv("MODRINTH_TOKEN")) // This is the default. Remember to have the MODRINTH_TOKEN environment variable set or else this will fail, or set it to whatever you want - just make sure it stays private!
    projectId.set("kinecraft-serialization") // This can be the project ID or the slug. Either will work!
    syncBodyFrom.set(rootProject.file("README.md").readText())
    versionType.set("release") // This is the default -- can also be `beta` or `alpha`
    uploadFile.set(tasks.remapJar)
    versionNumber.set("${project.version}-fabric")
    gameVersions.addAll(
        "1.18.2",
        "1.19",
        "1.19.1",
        "1.19.2",
        "1.19.3",
        "1.19.4",
        "1.20",
        "1.20.1",
    ) // Must be an array, even with only one version
    loaders.addAll(
        "fabric",
        "quilt",
    ) // Must also be an array - no need to specify this if you're using Loom or ForgeGradle
    dependencies {
        required.version("Ha28R6CL", "1.9.6+kotlin.1.8.22")
    }
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
