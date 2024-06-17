plugins {
    alias(catalog.plugins.neoforge.gradle)
    alias(catalog.plugins.kotlin.jvm)
    alias(catalog.plugins.kotlin.plugin.serialization)
    `maven-publish`
}

base { archivesName.set("${rootProject.base.archivesName.get()}-${project.name}") }

minecraft {
    runs {
        afterEvaluate {
            clear()
        }
    }
}

jarJar.enable()

dependencies {
    implementation(catalog.neoforge)
    implementation(catalog.kotlinx.serialization.core)
    implementation(catalog.kotlinx.serialization.json)
    implementation(catalog.kotlin.reflect)
    jarJar(project(":neoforge"))
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
