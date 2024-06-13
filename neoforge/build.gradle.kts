plugins {
    alias(libs.plugins.neoforge.gradle)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    `maven-publish`
}

repositories {
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://maven.neoforged.net/releases") { name = "NeoForge" }
}

subsystems {
    parchment {
        minecraftVersion = libs.versions.minecraft.get()
        mappingsVersion = libs.versions.parchmentmc.get()
    }
}

minecraft {
    runs {
        afterEvaluate {
            clear()
        }
    }
}

jarJar.enable()

dependencies {
    implementation(libs.forge)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.reflect)
    jarJar(project(":common:neoforgeTransform"))
}

tasks {
    jar {
        archiveClassifier.set("dev")
        from("LICENSE") { rename { "${it}_KinecraftSerialization" } }
        manifest.attributes(
            "FMLModType" to "GAMELIBRARY"
        )
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    jarJar.configure {
        archiveClassifier.set("")
    }

    sourcesJar { from(rootProject.sourceSets.main.get().allSource) }
}

publishing {
    publications {
        create<MavenPublication>(rootProject.name) {
            groupId = "${rootProject.group}"
            artifactId = base.archivesName.get()
            version = "${rootProject.version}"
            from(components.getByName("java"))
        }
    }
}
