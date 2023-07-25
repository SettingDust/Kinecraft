import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
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

            filesMatching(listOf("fabric.mod.json", "META-INF/mods.toml")) {
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

