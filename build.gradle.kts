import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.lang.Closure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `maven-publish`

    alias(catalog.plugins.git.version)
    alias(catalog.plugins.idea.ext)
    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)
    alias(catalog.plugins.fabric.loom) apply false
    alias(catalog.plugins.neoforge.gradle) apply false

    alias(catalog.plugins.shadow)
}

apply("https://github.com/SettingDust/MinecraftGradleScripts/raw/main/gradle_issue_15754.gradle.kts")

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
        withJavadocJar()
    }

    dependencies {
        implementation(rootProject.catalog.mixin)
        annotationProcessor(variantOf(rootProject.catalog.mixin) {
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
    group = rootProject.group
    version = rootProject.version

    base { archivesName.set("${rootProject.base.archivesName.get()}${project.path.replace(":", "-")}") }

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

val shadowSourcesJar by tasks.creating(ShadowJar::class) {
    mergeServiceFiles()
    archiveClassifier.set("sources")
    from(subprojects.map { it.sourceSets.main.get().allSource })

    doFirst {
        manifest {
            from(source.filter { it.name.equals("MANIFEST.MF") }.toList())
        }
    }
}

rootProject.publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = base.archivesName.get()

            artifact(shadowSourcesJar) {
                classifier = "sources"
            }
        }
    }

    repositories {
        maven("file://${rootProject.projectDir}/publish")
    }
}
