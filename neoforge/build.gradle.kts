import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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

dependencies {
    implementation(project(":common"))

    jarJar(project(":versions:1.21"))
}

tasks {
    processResources { from(project(":common").sourceSets.main.get().resources) }
    compileKotlin {
        source(project(":common").sourceSets.main.get().kotlin)
        compilerOptions { jvmTarget = JvmTarget.JVM_21 }
    }
    compileJava { source(project(":common").sourceSets.main.get().java) }

    sourcesJar { from(project(":common").sourceSets.main.get().allSource) }

    jar { manifest { attributes("FMLModType" to "GAMELIBRARY") } }
}

rootProject.publishing {
    publications {
        named<MavenPublication>("maven") { artifact(tasks.jar) { classifier = "neoforge" } }
    }
}
