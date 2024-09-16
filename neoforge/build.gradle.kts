import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`

    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)

    alias(catalog.plugins.neoforge.moddev)
}

val mod_id: String by rootProject

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    withSourcesJar()

    toolchain { languageVersion = JavaLanguageVersion.of(21) }
}

kotlin { jvmToolchain(21) }

neoForge {
    version = catalog.neoforge.get().version!!

    runs { create("client") { client() } }

    mods { create(mod_id) { sourceSet(sourceSets.main.get()) } }
}

dependencies { implementation(project(":common")) }

tasks {
    withType<ProcessResources> { from(project(":common").sourceSets.main.get().resources) }
    withType<KotlinCompile> {
        source(project(":common").sourceSets.main.get().allSource)
        compilerOptions { jvmTarget = JvmTarget.JVM_21 }
    }
    withType<JavaCompile> { source(project(":common").sourceSets.main.get().allSource) }

    sourcesJar { from(project(":common").sourceSets.main.get().allSource) }

    jar {
        manifest {
            from("src/main/resources/META-INF/MANIFEST.MF")
            "Automatic-Module-Name" to project.path
        }
    }
}

rootProject.publishing {
    publications {
        named<MavenPublication>("maven") { artifact(tasks.jar) { classifier = "neoforge" } }
    }
}
