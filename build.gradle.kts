import groovy.lang.Closure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    alias(libs.plugins.git.version)
    alias(libs.plugins.idea.ext)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.fabric.loom) apply false
    alias(libs.plugins.neoforge.gradle) apply false

    alias(libs.plugins.shadow)
}

group = "${project.property("group")}"

val gitVersion: Closure<String> by extra
version = gitVersion()

base {
    archivesName.set(properties["archive_base_name"].toString())
}

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

    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
}

subprojects {
    apply(plugin = "java")

    group = rootProject.group
    version = rootProject.version

    base { archivesName.set("${rootProject.base.archivesName.get()}${project.path.replace(":", "-")}") }

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
            val properties = mapOf(
                "id" to "kinecraft_serialization",
                "version" to project.version,
                "group" to project.group,
                "name" to "Kinecraft Serialization",
                "description" to "Kotlin serialization for Minecraft classes",
                "author" to "SettingDust",
                "source" to "https://github.com/SettingDust/kinecraft-serialization",
            )
            from(rootProject.sourceSets.main.get().resources)
            inputs.properties(properties)

            filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
                expand(properties)
            }
        }

        test {
            enabled = false
        }
    }
}
