import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.vanilla.gradle)
}

group = "${project.property("group")}"

version = "${project.property("version")}"

allprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    repositories {
        mavenCentral()
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }

    dependencies {
        implementation(rootProject.libs.mixin)
        annotationProcessor(variantOf(rootProject.libs.mixin) {
            classifier("processor")
        })
    }
}

minecraft { version(libs.versions.minecraft.get()) }

dependencies {
    api(libs.kotlinx.serialization.core)
    api(libs.kotlinx.serialization.json)
    api(libs.kotlin.reflect)

    testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

subprojects {
    apply(plugin = "java")

    java {
        // Still required by IDEs such as Eclipse and Visual Studio Code
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21

        // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build"
        // task if it is present.
        // If you remove this line, sources will not be generated.
        withSourcesJar()

        // If this mod is going to be a library, then it should also generate Javadocs in order to
        // aid with development.
        // Uncomment this line to generate them.
        // withJavadocJar()
    }

    tasks {
        withType<ProcessResources> {
            from(rootProject.sourceSets.main.get().resources)
            inputs.property("version", project.version)
            inputs.property("group", project.group)

            filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
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
