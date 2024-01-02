import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.spongepowered.asm.gradle.plugins.MixinExtension

buildscript { dependencies { classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT") } }

plugins {
    alias(libs.plugins.forge.gradle)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.minotaur)
    `maven-publish`
}

apply(plugin = "org.spongepowered.mixin")

base { archivesName.set("${rootProject.name}-forge") }

configure<MixinExtension> { add("main", "${rootProject.name}.refmap.json") }

repositories {
    maven {
        name = "Forge"
        url = uri("https://maven.minecraftforge.net/")
    }
    mavenCentral()
}

minecraft { mappings("official", libs.versions.minecraft.get()) }

dependencies {
    minecraft(libs.forge)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.hocon)
    implementation(libs.kotlin.reflect)

    //    jarJar(rootProject)
}

tasks {
    withType<ProcessResources> { from(rootProject.sourceSets.main.get().resources) }
    withType<KotlinCompile> { source(rootProject.sourceSets.main.get().allSource) }
    jar {
        from("LICENSE") { rename { "${it}_KinecraftSerialization" } }
        finalizedBy("reobfJar")
        manifest.attributes(
            // 1.16.5 no GAMELIBRARY
            "FMLModType" to "MOD",
        )
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    sourcesJar { from(rootProject.sourceSets.main.get().allSource) }
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
    uploadFile.set(tasks.jar)
    versionNumber.set("${project.version}-forge")
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
    ) // Must be an array, even with only one version
    loaders.add(
        "forge",
    ) // Must also be an array - no need to specify this if you're using Loom or ForgeGradle
    dependencies { required.project("kotlin-for-forge") }
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
