import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.minotaur)
    alias(libs.plugins.vanilla.gradle)
    `maven-publish`
}

group = "${project.property("group")}"
version = "${project.property("version")}"

repositories {
    mavenCentral()
}

minecraft {
    version(libs.versions.minecraft.get())
}

dependencies {
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.hocon)
    implementation(libs.kotlin.reflect)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

subprojects {
    apply(plugin = "java")

    java {
        // Still required by IDEs such as Eclipse and Visual Studio Code
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task if it is present.
        // If you remove this line, sources will not be generated.
        withSourcesJar()

        // If this mod is going to be a library, then it should also generate Javadocs in order to aid with development.
        // Uncomment this line to generate them.
        // withJavadocJar()
    }

    tasks {
        withType<ProcessResources> {
            from(rootProject.sourceSets.main.get().resources)
            inputs.property("version", project.version)
            inputs.property("group", project.group)

            filesMatching(listOf("fabric.mod.json", "mods.toml")) {
                expand(
                    "id" to "kinecraft_serialization",
                    "version" to project.version,
                    "group" to project.group,
                    "name" to "Kinecraft Serialization",
                    "description" to "Kotlin serialization for Minecraft classes",
                    "author" to "SettingDust",
                    "source" to "https://github.com/SettingDust/kinecraft-serialization",
                )
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("kinecraft-serialization") {
            groupId = "${rootProject.group}"
            artifactId = "kinecraft-serialization"
            version = "${rootProject.version}"
            from(components.getByName("java"))
        }
    }
}

val finalJar by tasks.registering(Jar::class) {
    dependsOn(":fabric:remapJar", ":forge:jar")
    from(
        zipTree(project(":fabric").tasks.getByName("remapJar").outputs.files.first()),
        zipTree(project(":forge").tasks.getByName("jar").outputs.files.first()),
    )
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//    from(project(":quilt").tasks.getByName("remapJar"))

    archiveBaseName.set("kinecraft-serialization")
    archiveVersion.set("${project.version}")
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN")) // This is the default. Remember to have the MODRINTH_TOKEN environment variable set or else this will fail, or set it to whatever you want - just make sure it stays private!
    projectId.set("kinecraft-serialization") // This can be the project ID or the slug. Either will work!
    syncBodyFrom.set(rootProject.file("README.md").readText())
    versionType.set("release") // This is the default -- can also be `beta` or `alpha`
    uploadFile.set(finalJar) // With Loom, this MUST be set to `remapJar` instead of `jar`!
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
        "forge",
    ) // Must also be an array - no need to specify this if you're using Loom or ForgeGradle
    dependencies {
        required.version("Ha28R6CL", "1.9.6+kotlin.1.8.22")
    }
}
